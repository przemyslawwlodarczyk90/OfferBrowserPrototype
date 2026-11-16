package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.infrastructure.scheduler.ScraperScheduler;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService.Mode;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService.RunInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scrapers")
@Tag(name = "Scrapers", description = "Run and monitor the per-portal scrapers (No Fluff Jobs, theprotocol.it, Just Join IT) independently.")
@PreAuthorize("isAuthenticated()")
public class ScraperController {

    private final ScraperJobService jobService;
    private final ObjectProvider<ScraperScheduler> scheduler;

    public ScraperController(ScraperJobService jobService, ObjectProvider<ScraperScheduler> scheduler) {
        this.jobService = jobService;
        this.scheduler = scheduler;
    }

    @GetMapping
    @Operation(summary = "Status of all portals", description = "Whether each portal's scraper is running, enabled, and the result of its last run.")
    public List<Map<String, Object>> statuses() {
        return Arrays.stream(OfferSource.values()).map(this::status).toList();
    }

    @GetMapping("/{source}")
    @Operation(summary = "Status of one portal")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status returned"),
            @ApiResponse(responseCode = "400", description = "Unknown source")
    })
    public Map<String, Object> status(
            @Parameter(description = "NoFluff, TheProtocol or JustJoin") @PathVariable String source) {
        return status(parse(source));
    }

    @PostMapping("/{source}/run")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Run scraper + import for one portal",
               description = "Starts in the background and returns 202 with the job; poll GET /api/scrapers/{source}. "
                       + "Returns 409 when this portal is already running, 423 when scraping is disabled.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Job started"),
            @ApiResponse(responseCode = "403", description = "Admin only"),
            @ApiResponse(responseCode = "409", description = "Already running"),
            @ApiResponse(responseCode = "423", description = "Scraper disabled in configuration")
    })
    public ResponseEntity<Map<String, Object>> run(@PathVariable String source) {
        return start(parse(source), Mode.SCRAPE_AND_IMPORT);
    }

    @PostMapping("/{source}/import-file")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Import the portal's last scraped JSON file", description = "Imports without scraping (e.g. after a manual run of the Python script).")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Import started"),
            @ApiResponse(responseCode = "409", description = "Portal busy")
    })
    public ResponseEntity<Map<String, Object>> importFile(@PathVariable String source) {
        return start(parse(source), Mode.IMPORT_ONLY);
    }

    private ResponseEntity<Map<String, Object>> start(OfferSource source, Mode mode) {
        return jobService.start(source, mode, "manual")
                .map(run -> ResponseEntity.status(HttpStatus.ACCEPTED).body(toMap(run)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                        "error", "Scraper " + source.displayName() + " już pracuje — poczekaj na zakończenie",
                        "source", source.value())));
    }

    private Map<String, Object> status(OfferSource source) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("source", source.value());
        m.put("displayName", source.displayName());
        m.put("enabled", jobService.isScrapingEnabled(source));
        m.put("running", jobService.isRunning(source));
        m.put("lastRun", jobService.lastRun(source).map(ScraperController::toMap).orElse(null));
        ScraperScheduler s = scheduler.getIfAvailable();
        m.put("nextScheduledRun", s == null ? null : s.nextScheduledRun(source).map(Object::toString).orElse(null));
        m.put("todaysRun", s == null ? null : s.todaysRun(source)
                .map(e -> Map.of("runAt", e.runAt().toString(), "started", e.started()))
                .orElse(null));
        return m;
    }

    private static Map<String, Object> toMap(RunInfo run) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("jobId", run.jobId());
        m.put("source", run.source());
        m.put("trigger", run.trigger());
        m.put("mode", run.mode());
        m.put("status", run.status());
        m.put("startedAt", run.startedAt());
        m.put("finishedAt", run.finishedAt());
        m.put("message", run.message());
        m.put("scraper", run.scraperSummary());
        m.put("import", run.importResult());
        return m;
    }

    private static OfferSource parse(String source) {
        return OfferSource.fromParam(source)
                .orElseThrow(() -> new IllegalArgumentException("Podaj źródło: " + OfferSource.supportedValues()));
    }
}
