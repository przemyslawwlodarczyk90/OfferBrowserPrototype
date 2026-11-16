package com.example.offerbrowserprototype.infrastructure.backup;

import com.example.offerbrowserprototype.domain.exception.ConflictException;
import com.example.offerbrowserprototype.domain.exception.NotFoundException;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheInvalidator;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Full database backup to a JSON file and restore from it.
 * <p>
 * Works on JDBC metadata, so every table of the application schema is included
 * (users, offers with their requirement collections, statuses, notes, flags,
 * scraper search URLs, …) — also tables added later — without code changes.
 * <ul>
 *   <li><b>Backup</b> — one consistent read (repeatable-read transaction), streamed
 *       to {@code backup.dir} (default {@code backups} in the application folder),
 *       written to a temp file and moved into place.</li>
 *   <li><b>Restore</b> — validates the file, saves a safety backup of the current
 *       state, then in ONE transaction deletes the rows of every table present in
 *       the file (children first) and inserts the backup rows (parents first, by
 *       foreign keys), finally moves identity sequences past the restored ids. Any
 *       error rolls everything back — the database stays as it was.</li>
 * </ul>
 * Tables that exist in the database but not in the file are left untouched.
 */
@Service
public class DatabaseJsonBackupService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseJsonBackupService.class);

    public static final String FORMAT = "offerbrowser-db-backup";
    public static final int VERSION = 1;
    static final Pattern FILE_NAME = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]{0,150}\\.json$");
    private static final DateTimeFormatter STAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final int BATCH = 500;

    public record BackupInfo(String fileName, long size, String createdAt, String createdBy, String kind,
                             Map<String, Long> rowCounts, long totalRows) {}

    public record RestoreResult(String fileName, Map<String, Long> restoredRows, long totalRows,
                                List<String> untouchedTables, String safetyBackup) {}

    private final DataSource dataSource;
    private final ObjectMapper mapper;
    private final ObjectProvider<ScraperJobService> scrapers;
    private final ObjectProvider<OfferCacheInvalidator> caches;
    private final Path dir;
    private final ReentrantLock lock = new ReentrantLock();

    public DatabaseJsonBackupService(DataSource dataSource, ObjectMapper mapper,
                                     ObjectProvider<ScraperJobService> scrapers,
                                     ObjectProvider<OfferCacheInvalidator> caches,
                                     @Value("${backup.dir:backups}") String dir) {
        this.dataSource = dataSource;
        this.mapper = mapper;
        this.scrapers = scrapers;
        this.caches = caches;
        this.dir = Path.of(dir == null || dir.isBlank() ? "backups" : dir.trim());
    }

    public Path directory() {
        return dir;
    }

    // ── Backup ───────────────────────────────────────────────────────────────

    /** @param kind "manual", "pre-restore", … — part of the file name */
    public BackupInfo createBackup(String username, String kind) {
        if (!lock.tryLock()) {
            throw new ConflictException("Trwa inna operacja na kopii zapasowej — spróbuj za chwilę");
        }
        try {
            return writeBackup(username, kind);
        } finally {
            lock.unlock();
        }
    }

    private BackupInfo writeBackup(String username, String kind) {
        String fileName = "backup_" + LocalDateTime.now().format(STAMP) + "_" + kind + ".json";
        Path target = dir.resolve(fileName);
        Path tmp = null;
        Map<String, Long> counts = new LinkedHashMap<>();
        try {
            Files.createDirectories(dir);
            tmp = Files.createTempFile(dir, ".backup_", ".tmp");
            try (Connection c = dataSource.getConnection()) {
                boolean auto = c.getAutoCommit();
                int isolation = c.getTransactionIsolation();
                c.setAutoCommit(false);
                c.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ); // one consistent snapshot
                try (JsonGenerator g = mapper.getFactory().createGenerator(tmp.toFile(), JsonEncoding.UTF8)) {
                    List<String> tables = orderedTables(c);
                    for (String t : tables) {
                        counts.put(t, count(c, t));
                    }
                    g.useDefaultPrettyPrinter();
                    g.writeStartObject();
                    g.writeStringField("format", FORMAT);
                    g.writeNumberField("version", VERSION);
                    g.writeStringField("createdAt", OffsetDateTime.now().toString());
                    g.writeStringField("createdBy", username);
                    g.writeStringField("kind", kind);
                    g.writeStringField("database", c.getMetaData().getDatabaseProductName() + " "
                            + c.getMetaData().getDatabaseProductVersion());
                    g.writeObjectFieldStart("rowCounts");
                    for (Map.Entry<String, Long> e : counts.entrySet()) {
                        g.writeNumberField(e.getKey(), e.getValue());
                    }
                    g.writeEndObject();
                    g.writeArrayFieldStart("tables");
                    for (String t : tables) {
                        writeTable(c, t, g);
                    }
                    g.writeEndArray();
                    g.writeEndObject();
                } finally {
                    c.rollback();
                    c.setTransactionIsolation(isolation);
                    c.setAutoCommit(auto);
                }
            }
            try {
                Files.move(tmp, target, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING);
            }
            long total = counts.values().stream().mapToLong(Long::longValue).sum();
            logger.info("Database backup {} written by {}: {} tables, {} rows", target, username, counts.size(), total);
            return info(target).orElseThrow();
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Nie udało się utworzyć kopii bazy: " + e.getMessage(), e);
        } finally {
            if (tmp != null) {
                try {
                    Files.deleteIfExists(tmp);
                } catch (IOException ignored) {
                    // temp file — removed on the next successful backup at the latest
                }
            }
        }
    }

    private void writeTable(Connection c, String table, JsonGenerator g) throws SQLException, IOException {
        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM " + quote(c, table))) {
            ResultSetMetaData md = rs.getMetaData();
            int n = md.getColumnCount();
            g.writeStartObject();
            g.writeStringField("name", table);
            g.writeArrayFieldStart("columns");
            for (int i = 1; i <= n; i++) {
                g.writeStartObject();
                g.writeStringField("name", md.getColumnName(i));
                g.writeStringField("type", md.getColumnTypeName(i));
                g.writeEndObject();
            }
            g.writeEndArray();
            g.writeArrayFieldStart("rows");
            while (rs.next()) {
                g.writeStartArray();
                for (int i = 1; i <= n; i++) {
                    writeValue(g, rs, i, md.getColumnType(i));
                }
                g.writeEndArray();
            }
            g.writeEndArray();
            g.writeEndObject();
        }
    }

    private static void writeValue(JsonGenerator g, ResultSet rs, int i, int type) throws SQLException, IOException {
        switch (type) {
            case Types.BIT, Types.BOOLEAN -> {
                boolean v = rs.getBoolean(i);
                if (rs.wasNull()) g.writeNull(); else g.writeBoolean(v);
            }
            case Types.TINYINT, Types.SMALLINT, Types.INTEGER, Types.BIGINT -> {
                long v = rs.getLong(i);
                if (rs.wasNull()) g.writeNull(); else g.writeNumber(v);
            }
            case Types.NUMERIC, Types.DECIMAL, Types.REAL, Types.FLOAT, Types.DOUBLE -> {
                BigDecimal v = rs.getBigDecimal(i);
                if (v == null) g.writeNull(); else g.writeNumber(v);
            }
            case Types.DATE -> {
                LocalDate v = rs.getObject(i, LocalDate.class);
                if (v == null) g.writeNull(); else g.writeString(v.toString());
            }
            case Types.TIME -> {
                LocalTime v = rs.getObject(i, LocalTime.class);
                if (v == null) g.writeNull(); else g.writeString(v.toString());
            }
            case Types.TIMESTAMP -> {
                LocalDateTime v = rs.getObject(i, LocalDateTime.class);
                if (v == null) g.writeNull(); else g.writeString(v.toString());
            }
            case Types.TIMESTAMP_WITH_TIMEZONE -> {
                OffsetDateTime v = rs.getObject(i, OffsetDateTime.class);
                if (v == null) g.writeNull(); else g.writeString(v.toString());
            }
            case Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY, Types.BLOB -> {
                byte[] v = rs.getBytes(i);
                if (v == null) g.writeNull(); else g.writeString(Base64.getEncoder().encodeToString(v));
            }
            default -> {
                String v = rs.getString(i);
                if (v == null) g.writeNull(); else g.writeString(v);
            }
        }
    }

    // ── Listing / files ─────────────────────────────────────────────────────

    public List<BackupInfo> list() {
        if (!Files.isDirectory(dir)) {
            return List.of();
        }
        try (Stream<Path> files = Files.list(dir)) {
            return files.filter(p -> FILE_NAME.matcher(p.getFileName().toString()).matches())
                    .map(this::info).flatMap(Optional::stream)
                    .sorted(Comparator.comparing(BackupInfo::createdAt, Comparator.nullsLast(Comparator.reverseOrder())))
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Nie można odczytać katalogu kopii: " + e.getMessage(), e);
        }
    }

    /** Header only (streamed up to "tables"), so listing large backups stays cheap. */
    Optional<BackupInfo> info(Path file) {
        try (JsonParser p = new JsonFactory().createParser(file.toFile())) {
            if (p.nextToken() != JsonToken.START_OBJECT) {
                return Optional.empty();
            }
            String format = null, createdAt = null, createdBy = null, kind = null;
            Map<String, Long> counts = new LinkedHashMap<>();
            while (p.nextToken() == JsonToken.FIELD_NAME) {
                String field = p.currentName();
                p.nextToken();
                switch (field) {
                    case "format" -> format = p.getValueAsString();
                    case "createdAt" -> createdAt = p.getValueAsString();
                    case "createdBy" -> createdBy = p.getValueAsString();
                    case "kind" -> kind = p.getValueAsString();
                    case "rowCounts" -> {
                        while (p.nextToken() == JsonToken.FIELD_NAME) {
                            String t = p.currentName();
                            p.nextToken();
                            counts.put(t, p.getLongValue());
                        }
                    }
                    case "tables" -> {
                        // header is complete — the rows are not needed for the list
                        return FORMAT.equals(format) ? Optional.of(new BackupInfo(file.getFileName().toString(),
                                Files.size(file), createdAt, createdBy, kind, counts,
                                counts.values().stream().mapToLong(Long::longValue).sum())) : Optional.empty();
                    }
                    default -> p.skipChildren();
                }
            }
            return Optional.empty();
        } catch (IOException e) {
            logger.warn("Skipping unreadable backup {}: {}", file, e.getMessage());
            return Optional.empty();
        }
    }

    public Path file(String fileName) {
        Path f = resolve(fileName);
        if (!Files.isRegularFile(f)) {
            throw new NotFoundException("Nie ma kopii " + fileName);
        }
        return f;
    }

    public void delete(String fileName) {
        try {
            Files.delete(file(fileName));
            logger.info("Backup {} deleted", fileName);
        } catch (IOException e) {
            throw new IllegalStateException("Nie udało się usunąć kopii: " + e.getMessage(), e);
        }
    }

    /** Stores an uploaded file in the backup folder (validated as a backup) and returns its name. */
    public String storeUpload(String originalName, InputStream content) throws IOException {
        Files.createDirectories(dir);
        String base = originalName == null ? "kopia" : Path.of(originalName.replace('\\', '/')).getFileName().toString();
        base = base.replaceAll("(?i)\\.json$", "").replaceAll("[^A-Za-z0-9._-]", "_");
        if (base.isBlank() || base.startsWith(".")) base = "kopia";
        if (base.length() > 80) base = base.substring(0, 80);
        String fileName = "uploaded_" + LocalDateTime.now().format(STAMP) + "_" + base + ".json";
        Path target = dir.resolve(fileName);
        Files.copy(content, target);
        if (info(target).isEmpty()) {
            Files.deleteIfExists(target);
            throw new IllegalArgumentException("To nie jest plik kopii zapasowej tej aplikacji (brak pola \"format\": \""
                    + FORMAT + "\")");
        }
        return fileName;
    }

    private Path resolve(String fileName) {
        if (fileName == null || !FILE_NAME.matcher(fileName).matches()) {
            throw new IllegalArgumentException("Niepoprawna nazwa pliku kopii");
        }
        Path f = dir.resolve(fileName).normalize();
        if (!f.getParent().equals(dir.normalize())) {
            throw new IllegalArgumentException("Niepoprawna nazwa pliku kopii");
        }
        return f;
    }

    // ── Restore ──────────────────────────────────────────────────────────────

    public RestoreResult restore(String fileName, String username) {
        Path file = file(fileName);
        if (!lock.tryLock()) {
            throw new ConflictException("Trwa inna operacja na kopii zapasowej — spróbuj za chwilę");
        }
        try {
            ScraperJobService jobs = scrapers.getIfAvailable();
            if (jobs != null && Arrays.stream(OfferSource.values()).anyMatch(jobs::isRunning)) {
                throw new ConflictException("Pracuje scraper/import ofert — przywróć bazę po jego zakończeniu");
            }
            JsonNode root = readAndValidate(file);
            BackupInfo safety = writeBackup(username, "pre-restore");
            RestoreResult result = load(root, fileName, safety.fileName());
            caches.ifAvailable(OfferCacheInvalidator::invalidateAll);
            logger.warn("Database restored from {} by {} ({} rows); safety backup: {}",
                    fileName, username, result.totalRows(), safety.fileName());
            return result;
        } finally {
            lock.unlock();
        }
    }

    private JsonNode readAndValidate(Path file) {
        JsonNode root;
        try {
            root = mapper.readTree(file.toFile());
        } catch (IOException e) {
            throw new IllegalArgumentException("Plik kopii nie jest poprawnym JSON-em: " + e.getMessage());
        }
        if (root == null || !FORMAT.equals(root.path("format").asText())) {
            throw new IllegalArgumentException("To nie jest plik kopii zapasowej tej aplikacji");
        }
        if (root.path("version").asInt() > VERSION) {
            throw new IllegalArgumentException("Kopia pochodzi z nowszej wersji aplikacji (format " + root.path("version").asInt() + ")");
        }
        if (!root.path("tables").isArray() || root.path("tables").isEmpty()) {
            throw new IllegalArgumentException("Kopia nie zawiera żadnych tabel");
        }
        return root;
    }

    private RestoreResult load(JsonNode root, String fileName, String safetyBackup) {
        try (Connection c = dataSource.getConnection()) {
            boolean auto = c.getAutoCommit();
            c.setAutoCommit(false);
            try {
                List<String> order = orderedTables(c);
                Map<String, String> byLower = new HashMap<>();
                order.forEach(t -> byLower.put(t.toLowerCase(Locale.ROOT), t));

                Map<String, JsonNode> backupTables = new LinkedHashMap<>();
                List<String> unknown = new ArrayList<>();
                for (JsonNode t : root.path("tables")) {
                    String actual = byLower.get(t.path("name").asText().toLowerCase(Locale.ROOT));
                    if (actual == null) unknown.add(t.path("name").asText()); else backupTables.put(actual, t);
                }
                if (!unknown.isEmpty()) {
                    throw new IllegalArgumentException("Kopia zawiera tabele, których nie ma w bazie: " + unknown);
                }
                List<String> restored = order.stream().filter(backupTables::containsKey).toList();
                List<String> untouched = order.stream().filter(t -> !backupTables.containsKey(t)).toList();

                List<String> reverse = new ArrayList<>(restored);
                Collections.reverse(reverse);
                for (String t : reverse) {
                    try (Statement st = c.createStatement()) {
                        st.executeUpdate("DELETE FROM " + quote(c, t));
                    }
                }
                Map<String, Long> counts = new LinkedHashMap<>();
                for (String t : restored) {
                    counts.put(t, insertRows(c, t, backupTables.get(t)));
                }
                resetIdentities(c, restored);
                c.commit();
                return new RestoreResult(fileName, counts, counts.values().stream().mapToLong(Long::longValue).sum(),
                        untouched, safetyBackup);
            } catch (RuntimeException | SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(auto);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Przywracanie przerwane, baza bez zmian: " + rootMessage(e), e);
        }
    }

    private long insertRows(Connection c, String table, JsonNode t) throws SQLException {
        Map<String, Integer> types = columnTypes(c, table);
        List<String> columns = new ArrayList<>();
        for (JsonNode col : t.path("columns")) {
            columns.add(col.path("name").asText());
        }
        List<Integer> indexes = new ArrayList<>();
        List<String> targetColumns = new ArrayList<>();
        Map<String, String> lowerToActual = new HashMap<>();
        types.keySet().forEach(k -> lowerToActual.put(k.toLowerCase(Locale.ROOT), k));
        for (int i = 0; i < columns.size(); i++) {
            String actual = lowerToActual.get(columns.get(i).toLowerCase(Locale.ROOT));
            if (actual == null) {
                throw new IllegalArgumentException("Kolumna " + table + "." + columns.get(i) + " z kopii nie istnieje w bazie");
            }
            indexes.add(i);
            targetColumns.add(actual);
        }
        JsonNode rows = t.path("rows");
        if (!rows.isArray() || rows.isEmpty()) {
            return 0;
        }
        StringJoiner names = new StringJoiner(", "), marks = new StringJoiner(", ");
        for (String col : targetColumns) {
            names.add(quote(c, col));
            marks.add("?");
        }
        String sql = "INSERT INTO " + quote(c, table) + " (" + names + ") VALUES (" + marks + ")";
        long n = 0;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            for (JsonNode row : rows) {
                if (!row.isArray() || row.size() != columns.size()) {
                    throw new IllegalArgumentException("Uszkodzony wiersz w tabeli " + table + " (zła liczba kolumn)");
                }
                for (int k = 0; k < indexes.size(); k++) {
                    String col = targetColumns.get(k);
                    bind(ps, k + 1, row.get(indexes.get(k)), types.get(col), table + "." + col);
                }
                ps.addBatch();
                if (++n % BATCH == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
        }
        return n;
    }

    private static void bind(PreparedStatement ps, int i, JsonNode v, int type, String where) throws SQLException {
        if (v == null || v.isNull()) {
            ps.setNull(i, type);
            return;
        }
        try {
            switch (type) {
                case Types.BIT, Types.BOOLEAN -> ps.setBoolean(i, v.isBoolean() ? v.booleanValue() : Boolean.parseBoolean(v.asText()));
                case Types.TINYINT, Types.SMALLINT, Types.INTEGER, Types.BIGINT -> ps.setLong(i, v.asLong());
                case Types.NUMERIC, Types.DECIMAL, Types.REAL, Types.FLOAT, Types.DOUBLE -> ps.setBigDecimal(i, new BigDecimal(v.asText()));
                case Types.DATE -> ps.setObject(i, LocalDate.parse(v.asText()));
                case Types.TIME -> ps.setObject(i, LocalTime.parse(v.asText()));
                case Types.TIMESTAMP -> ps.setObject(i, LocalDateTime.parse(v.asText()));
                case Types.TIMESTAMP_WITH_TIMEZONE -> ps.setObject(i, OffsetDateTime.parse(v.asText()));
                case Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY, Types.BLOB -> ps.setBytes(i, Base64.getDecoder().decode(v.asText()));
                case Types.CHAR, Types.VARCHAR, Types.LONGVARCHAR, Types.NCHAR, Types.NVARCHAR, Types.LONGNVARCHAR, Types.CLOB ->
                        ps.setString(i, v.asText());
                default -> ps.setObject(i, v.asText(), type);
            }
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Niepoprawna wartość w " + where + ": " + v, e);
        }
    }

    private void resetIdentities(Connection c, List<String> tables) throws SQLException {
        boolean postgres = c.getMetaData().getDatabaseProductName().toLowerCase(Locale.ROOT).contains("postgres");
        DatabaseMetaData md = c.getMetaData();
        for (String t : tables) {
            try (ResultSet cols = md.getColumns(c.getCatalog(), c.getSchema(), t, null)) {
                while (cols.next()) {
                    if (!"YES".equalsIgnoreCase(cols.getString("IS_AUTOINCREMENT"))) continue;
                    String col = cols.getString("COLUMN_NAME");
                    long next;
                    try (Statement st = c.createStatement();
                         ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(" + quote(c, col) + "), 0) + 1 FROM " + quote(c, t))) {
                        rs.next();
                        next = rs.getLong(1);
                    }
                    try (Statement st = c.createStatement()) {
                        if (postgres) {
                            st.execute("SELECT setval(pg_get_serial_sequence('" + quote(c, t).replace("'", "''") + "', '"
                                    + col.replace("'", "''") + "'), " + next + ", false)");
                        } else {
                            st.execute("ALTER TABLE " + quote(c, t) + " ALTER COLUMN " + quote(c, col) + " RESTART WITH " + next);
                        }
                    }
                }
            }
        }
    }

    // ── Metadata ─────────────────────────────────────────────────────────────

    /** Tables of the application schema, parents before children (by foreign keys). */
    List<String> orderedTables(Connection c) throws SQLException {
        DatabaseMetaData md = c.getMetaData();
        List<String> tables = new ArrayList<>();
        try (ResultSet rs = md.getTables(c.getCatalog(), c.getSchema(), "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        }
        Collections.sort(tables);
        Map<String, Set<String>> parents = new HashMap<>();
        for (String t : tables) {
            Set<String> ps = new HashSet<>();
            try (ResultSet fk = md.getImportedKeys(c.getCatalog(), c.getSchema(), t)) {
                while (fk.next()) {
                    String parent = fk.getString("PKTABLE_NAME");
                    if (!parent.equals(t) && tables.contains(parent)) ps.add(parent);
                }
            }
            parents.put(t, ps);
        }
        List<String> ordered = new ArrayList<>();
        Set<String> done = new HashSet<>();
        while (ordered.size() < tables.size()) {
            boolean progress = false;
            for (String t : tables) {
                if (!done.contains(t) && done.containsAll(parents.get(t))) {
                    ordered.add(t);
                    done.add(t);
                    progress = true;
                }
            }
            if (!progress) {
                throw new IllegalStateException("Cykliczne klucze obce między tabelami — nie da się ustalić kolejności");
            }
        }
        return ordered;
    }

    private static Map<String, Integer> columnTypes(Connection c, String table) throws SQLException {
        Map<String, Integer> types = new LinkedHashMap<>();
        try (ResultSet rs = c.getMetaData().getColumns(c.getCatalog(), c.getSchema(), table, null)) {
            while (rs.next()) {
                types.put(rs.getString("COLUMN_NAME"), rs.getInt("DATA_TYPE"));
            }
        }
        return types;
    }

    private static long count(Connection c, String table) throws SQLException {
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + quote(c, table))) {
            rs.next();
            return rs.getLong(1);
        }
    }

    private static String quote(Connection c, String identifier) throws SQLException {
        String q = c.getMetaData().getIdentifierQuoteString();
        q = q == null || q.isBlank() ? "\"" : q.trim();
        return q + identifier.replace(q, q + q) + q;
    }

    private static String rootMessage(Throwable e) {
        Throwable t = e;
        while (t.getCause() != null && t.getCause() != t) t = t.getCause();
        if (e instanceof SQLException sql && sql.getNextException() != null) t = sql.getNextException();
        return t.getMessage();
    }
}
