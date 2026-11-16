package com.example.offerbrowserprototype.infrastructure.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stores users' CV files on disk in {@code cv.storage.dir} (by default the
 * application's working directory). The database keeps only the file's location
 * ({@code users.cv_url}) — this service turns an upload into such a location and back.
 * <p>
 * File name: {@code cv_<userId>_<yyyyMMdd-HHmmss>_<original name>}, so files of
 * different users never clash and the original name can be shown again.
 */
@Service
public class CvStorageService {

    private static final Logger logger = LoggerFactory.getLogger(CvStorageService.class);

    /** Allowed types: extension → content type; the first bytes are checked too. */
    static final Map<String, String> CONTENT_TYPES = Map.of(
            "pdf", "application/pdf",
            "doc", "application/msword",
            "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "odt", "application/vnd.oasis.opendocument.text",
            "rtf", "application/rtf");

    private static final byte[] PDF = {'%', 'P', 'D', 'F'};
    private static final byte[] ZIP = {'P', 'K', 3, 4};                        // docx, odt
    private static final byte[] OLE = {(byte) 0xD0, (byte) 0xCF, 0x11, (byte) 0xE0}; // doc
    private static final byte[] RTF = {'{', '\\', 'r', 't', 'f'};

    private static final Pattern STORED_NAME = Pattern.compile("^cv_\\d+_\\d{8}-\\d{6}_(.+)$");
    private static final DateTimeFormatter STAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    private static final int MAX_NAME_LENGTH = 120;

    public record StoredCv(String url, String fileName, long size, String contentType, Instant uploadedAt) {}

    private final String storageDir;
    private final long maxBytes;

    public CvStorageService(@Value("${cv.storage.dir:.}") String storageDir,
                            @Value("${cv.max-size-mb:10}") long maxSizeMb) {
        this.storageDir = storageDir == null || storageDir.isBlank() ? "." : storageDir.trim();
        this.maxBytes = maxSizeMb * 1024 * 1024;
    }

    public long maxBytes() {
        return maxBytes;
    }

    /**
     * Validates and writes the file (temp file + move, so a broken upload never leaves half a CV).
     *
     * @return location to store in {@code users.cv_url}
     * @throws IllegalArgumentException empty / too large / not a PDF, DOC(X), ODT or RTF document
     */
    public String store(Long userId, String originalName, long size, InputStream content) throws IOException {
        if (size <= 0) {
            throw new IllegalArgumentException("Plik jest pusty");
        }
        if (size > maxBytes) {
            throw new IllegalArgumentException("Plik jest za duży — maksymalny rozmiar to " + maxBytes / (1024 * 1024) + " MB");
        }
        String safeName = sanitize(originalName);
        String extension = extension(safeName)
                .filter(CONTENT_TYPES::containsKey)
                .orElseThrow(() -> new IllegalArgumentException("Dozwolone formaty CV: PDF, DOC, DOCX, ODT, RTF"));

        Path dir = Path.of(storageDir);
        Files.createDirectories(dir);
        String fileName = "cv_" + userId + "_" + LocalDateTime.now().format(STAMP) + "_" + safeName;
        Path target = dir.resolve(fileName).normalize();
        Path tmp = Files.createTempFile(dir, ".cv_upload_", ".tmp");
        try {
            long written = Files.copy(content, tmp, StandardCopyOption.REPLACE_EXISTING);
            if (written > maxBytes) {
                throw new IllegalArgumentException("Plik jest za duży — maksymalny rozmiar to " + maxBytes / (1024 * 1024) + " MB");
            }
            if (!hasSignature(tmp, extension)) {
                throw new IllegalArgumentException("Zawartość pliku nie pasuje do rozszerzenia ." + extension
                        + " — wybierz prawdziwy dokument PDF, DOC, DOCX, ODT lub RTF");
            }
            try {
                Files.move(tmp, target, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(tmp);
        }
        String url = storageDir.replace('\\', '/').replaceAll("/+$", "") + "/" + fileName;
        logger.info("CV of user {} stored as {} ({} B)", userId, url, size);
        return url;
    }

    /** The stored file, when it still exists. */
    public Optional<StoredCv> find(String url) {
        if (url == null || url.isBlank()) {
            return Optional.empty();
        }
        Path path = Path.of(url);
        if (!Files.isRegularFile(path)) {
            return Optional.empty();
        }
        try {
            String name = displayName(path.getFileName().toString());
            String type = extension(name).map(CONTENT_TYPES::get).orElse("application/octet-stream");
            return Optional.of(new StoredCv(url, name, Files.size(path), type,
                    Files.getLastModifiedTime(path).toInstant()));
        } catch (IOException e) {
            logger.warn("Cannot read CV file {}: {}", url, e.getMessage());
            return Optional.empty();
        }
    }

    public Path path(String url) {
        return Path.of(url);
    }

    public void delete(String url) {
        if (url == null || url.isBlank()) {
            return;
        }
        try {
            Files.deleteIfExists(Path.of(url));
        } catch (IOException e) {
            logger.warn("Could not delete CV file {}: {}", url, e.getMessage());
        }
    }

    /** "cv_5_20261022-143000_Jan Kowalski CV.pdf" → "Jan Kowalski CV.pdf". */
    static String displayName(String storedName) {
        Matcher m = STORED_NAME.matcher(storedName);
        return m.matches() ? m.group(1) : storedName;
    }

    /** File name without directories, control or reserved characters; keeps Polish letters. */
    static String sanitize(String originalName) {
        String name = originalName == null ? "" : originalName;
        name = name.substring(Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\')) + 1);
        name = Normalizer.normalize(name, Normalizer.Form.NFC)
                .replaceAll("[\\p{Cntrl}<>:\"|?*]", "")
                .replaceAll("\\s+", " ")
                .replaceAll("^[ .]+|[ .]+$", "");
        if (name.isBlank() || name.indexOf('.') <= 0) {
            name = "CV" + extension(name).map(e -> "." + e).orElse("");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            String ext = extension(name).map(e -> "." + e).orElse("");
            name = name.substring(0, MAX_NAME_LENGTH - ext.length()) + ext;
        }
        return name;
    }

    private static Optional<String> extension(String name) {
        int dot = name.lastIndexOf('.');
        return dot < 0 || dot == name.length() - 1
                ? Optional.empty()
                : Optional.of(name.substring(dot + 1).toLowerCase(Locale.ROOT));
    }

    private static boolean hasSignature(Path file, String extension) throws IOException {
        byte[] head = new byte[8];
        int read;
        try (InputStream in = Files.newInputStream(file)) {
            read = in.readNBytes(head, 0, head.length);
        }
        byte[] expected = switch (extension) {
            case "pdf" -> PDF;
            case "docx", "odt" -> ZIP;
            case "doc" -> OLE;
            case "rtf" -> RTF;
            default -> null;
        };
        return expected != null && read >= expected.length
                && Arrays.equals(Arrays.copyOf(head, expected.length), expected);
    }
}
