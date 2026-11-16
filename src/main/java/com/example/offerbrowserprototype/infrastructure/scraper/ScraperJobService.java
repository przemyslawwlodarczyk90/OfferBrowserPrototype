package com.example.offerbrowserprototype.infrastructure.scraper;

import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.infrastructure.service.OfferImportService;
import com.example.offerbrowserprototype.infrastructure.service.OfferImportService.ImportResult;
import com.example.offerbrowserprototype.infrastructure.service.PythonProcessRunner;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Runs each portal's scraper and import independently. A portal can have only
 * one run at a time; across portals only one scraper process runs at once (the
 * other waits as QUEUED) to keep CPU/RAM/network usage bounded. A failure of one
 * portal never affects the other.
 */
@Service
public class ScraperJobService {

    private static final Logger logger = LoggerFactory.getLogger(ScraperJobService.class);

    public enum Mode { SCRAPE_AND_IMPORT, IMPORT_ONLY }

    public enum Status { QUEUED, RUNNING, SUCCESS, PARTIAL, FAILED }

    public record RunInfo(
            String jobId,
            String source,
            String trigger,
            Mode mode,
            Status status,
            Instant startedAt,
            Instant finishedAt,
            String message,
            Map<String, Object> scraperSummary,
            Map<String, Object> importResult
    ) {
        RunInfo with(Status status, String message) {
            Instant end = status == Status.QUEUED || status == Status.RUNNING ? null : Instant.now();
            return new RunInfo(jobId, source, trigger, mode, status, startedAt, end, message, scraperSummary, importResult);
        }

        RunInfo withResults(Map<String, Object> summary, Map<String, Object> imported) {
            return new RunInfo(jobId, source, trigger, mode, status, startedAt, finishedAt, message, summary, imported);
        }
    }

    public static class ScraperDisabledException extends RuntimeException {
        public ScraperDisabledException(String message) {
            super(message);
        }
    }

    private final ScraperSettings settings;
    private final ScriptLocator scriptLocator;
    private final PythonProcessRunner runner;
    private final OfferImportService importService;
    private final ObjectMapper objectMapper;
    private final KnownOfferUrls knownOfferUrls;
    private final SearchUrlsFile searchUrlsFile;

    private final Map<OfferSource, AtomicBoolean> running = new EnumMap<>(OfferSource.class);
    private final Map<OfferSource, RunInfo> lastRuns = new EnumMap<>(OfferSource.class);
    private final Semaphore scraperSlot = new Semaphore(1, true);
    private final ExecutorService executor;

    public ScraperJobService(ScraperSettings settings, ScriptLocator scriptLocator, PythonProcessRunner runner,
                             OfferImportService importService, ObjectMapper objectMapper, KnownOfferUrls knownOfferUrls,
                             SearchUrlsFile searchUrlsFile) {
        this.settings = settings;
        this.scriptLocator = scriptLocator;
        this.runner = runner;
        this.importService = importService;
        this.objectMapper = objectMapper;
        this.knownOfferUrls = knownOfferUrls;
        this.searchUrlsFile = searchUrlsFile;
        for (OfferSource s : OfferSource.values()) {
            running.put(s, new AtomicBoolean(false));
        }
        this.executor = Executors.newFixedThreadPool(OfferSource.values().length, r -> {
            Thread t = new Thread(r, "scraper-job");
            t.setDaemon(true);
            return t;
        });
    }

    @PreDestroy
    void shutdown() {
        executor.shutdownNow();
    }

    /**
     * Starts a run in the background.
     *
     * @return the queued run, or empty when this portal is already running
     * @throws ScraperDisabledException when scraping is switched off for the portal
     */
    public Optional<RunInfo> start(OfferSource source, Mode mode, String trigger) {
        if (mode == Mode.SCRAPE_AND_IMPORT && !settings.of(source).enabled()) {
            throw new ScraperDisabledException("Scraper " + source.displayName()
                    + " jest wyłączony w konfiguracji (scraper." + source.name().toLowerCase() + ".enabled=false)");
        }
        if (!running.get(source).compareAndSet(false, true)) {
            logger.info("[{}] Run requested by {} skipped — previous run still in progress", source.value(), trigger);
            return Optional.empty();
        }
        RunInfo info = new RunInfo(UUID.randomUUID().toString(), source.value(), trigger, mode, Status.QUEUED,
                Instant.now(), null, "Oczekuje na rozpoczęcie", null, null);
        store(source, info);
        try {
            executor.submit(() -> execute(source, info));
        } catch (RuntimeException e) {
            running.get(source).set(false);
            store(source, info.with(Status.FAILED, "Nie udało się uruchomić zadania: " + e.getMessage()));
            throw e;
        }
        return Optional.of(info);
    }

    public boolean isRunning(OfferSource source) {
        return running.get(source).get();
    }

    public Optional<RunInfo> lastRun(OfferSource source) {
        synchronized (lastRuns) {
            return Optional.ofNullable(lastRuns.get(source));
        }
    }

    public boolean isScrapingEnabled(OfferSource source) {
        return settings.of(source).enabled();
    }

    private void store(OfferSource source, RunInfo info) {
        synchronized (lastRuns) {
            lastRuns.put(source, info);
        }
    }

    private void execute(OfferSource source, RunInfo queued) {
        RunInfo info = queued;
        try {
            Map<String, Object> summary = null;
            if (queued.mode() == Mode.SCRAPE_AND_IMPORT) {
                scraperSlot.acquire();
                try {
                    info = info.with(Status.RUNNING, "Scraper pracuje…");
                    store(source, info);
                    ScrapeOutcome outcome = scrape(source);
                    summary = outcome.summary();
                    if (!outcome.ok()) {
                        store(source, info.withResults(summary, null).with(Status.FAILED, outcome.message()));
                        return;
                    }
                } finally {
                    scraperSlot.release();
                }
            } else {
                info = info.with(Status.RUNNING, "Import pliku…");
                store(source, info);
            }

            ImportResult imported = importService.importOffersFromJson(settings.of(source).outputFile(), source);
            boolean partial = summary != null && "PARTIAL".equals(summary.get("status"))
                    || imported.errors() > 0 || imported.rejected() > 0;
            String message = "Zaimportowano " + imported.imported() + " nowych ofert, zaktualizowano "
                    + imported.updated() + ", pominięto " + imported.skipped()
                    + (imported.rejected() > 0 ? ", odrzucono " + imported.rejected() : "");
            store(source, info.withResults(summary, imported.toMap())
                    .with(partial ? Status.PARTIAL : Status.SUCCESS, message));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            store(source, info.with(Status.FAILED, "Przerwano zadanie"));
        } catch (Exception e) {
            logger.error("[{}] Run failed", source.value(), e);
            store(source, info.with(Status.FAILED, e.getMessage()));
        } finally {
            running.get(source).set(false);
            lastRun(source).ifPresent(r -> logger.info("[{}] Run {} finished: {} — {}",
                    source.value(), r.jobId(), r.status(), r.message()));
        }
    }

    private record ScrapeOutcome(boolean ok, String message, Map<String, Object> summary) {}

    private ScrapeOutcome scrape(OfferSource source) {
        ScraperSettings.Portal portal = settings.of(source);
        String script = scriptLocator.resolve(portal.bulkScript());
        File output = new File(portal.outputFile());
        long before = output.exists() ? output.lastModified() : -1;

        // the admin's search URLs (database) are passed to the scraper; an empty list stops the run here
        Path searchFile = searchUrlsFile.writeTempFile(source);
        // offers already in the database are passed to the scraper, which skips their detail pages;
        // a database error stops the run here — the portal is not scraped "as if empty"
        Path knownUrlsFile = null;
        PythonProcessRunner.Result result;
        try {
            knownUrlsFile = knownOfferUrls.supports(source) ? knownOfferUrls.writeTempFile(source) : null;
            List<String> args = new ArrayList<>(List.of("--urls-file", searchFile.toString()));
            if (knownUrlsFile != null) {
                args.addAll(List.of("--known-urls", knownUrlsFile.toString()));
            }
            result = runner.run(script, args, portal.bulkTimeout());
        } finally {
            deleteQuietly(searchFile);
            deleteQuietly(knownUrlsFile);
        }
        Map<String, Object> summary = parseSummary(result.lastStdoutLine());

        if (result.timedOut()) {
            return new ScrapeOutcome(false, "Przekroczono limit czasu " + portal.bulkTimeout().toMinutes()
                    + " min — proces zatrzymany, poprzedni plik bez zmian", summary);
        }
        if (result.exitCode() != 0) {
            String reason = summary != null && summary.get("message") != null
                    ? summary.get("message").toString()
                    : describeExit(result.exitCode()) + tail(result.stderrTail());
            return new ScrapeOutcome(false, reason, summary);
        }
        if (summary == null && output.lastModified() == before) {
            return new ScrapeOutcome(false, "Scraper zakończył się bez zapisania nowego pliku" + tail(result.stderrTail()), null);
        }
        return new ScrapeOutcome(true, "OK", summary);
    }

    private static void deleteQuietly(Path file) {
        if (file == null) {
            return;
        }
        try {
            Files.deleteIfExists(file);
        } catch (java.io.IOException ignored) {
            // temp file — the OS cleans it up eventually
        }
    }

    private Map<String, Object> parseSummary(String line) {
        if (line == null || !line.startsWith("{")) {
            return null;
        }
        try {
            return objectMapper.readValue(line, new TypeReference<LinkedHashMap<String, Object>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    public static String describeExit(int code) {
        return switch (code) {
            case 2 -> "Nieprawidłowe parametry lub adres";
            case 3 -> "Serwis zablokował dostęp (ochrona antybotowa) — scraper przerwał pracę";
            case 4 -> "Ogłoszenie nie istnieje lub zostało usunięte";
            case 5 -> "Zmieniła się struktura strony — parser wymaga aktualizacji";
            case 6 -> "Błąd sieci lub serwisu po kilku próbach";
            default -> "Skrypt zakończył się kodem " + code;
        };
    }

    private static String tail(String stderr) {
        if (stderr == null || stderr.isBlank()) return "";
        String[] lines = stderr.strip().split("\\R");
        return " (" + lines[lines.length - 1].trim() + ")";
    }
}
