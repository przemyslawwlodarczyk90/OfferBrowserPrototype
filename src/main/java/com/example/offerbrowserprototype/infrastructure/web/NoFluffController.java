package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.facade.OfferFacade;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService;
import com.example.offerbrowserprototype.infrastructure.service.OfferImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/python-script")
@Tag(name = "Import & Scripts", description = "Endpoints for importing job offers: run the No Fluff Jobs scraper, import from JSON body (per portal), or import a single offer from a URL (No Fluff Jobs or theprotocol.it).")
@PreAuthorize("isAuthenticated()")
public class NoFluffController {

    private static final Logger logger = LoggerFactory.getLogger(NoFluffController.class);

    private final ScraperJobService scraperJobService;
    private final OfferImportService offerImportService;
    private final OfferFacade offerFacade;
    private final UserRepository userRepository;

    public NoFluffController(ScraperJobService scraperJobService,
                             OfferImportService offerImportService,
                             OfferFacade offerFacade,
                             UserRepository userRepository) {
        this.scraperJobService = scraperJobService;
        this.offerImportService = offerImportService;
        this.offerFacade = offerFacade;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Run NoFluff scraper script",
               description = "Starts the No Fluff Jobs scraper in the background (admin only). " +
                             "Returns 202 immediately — offers are imported automatically after the script finishes. " +
                             "Same as POST /api/scrapers/NoFluff/run; 409 when it is already running.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Script started in background"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Admin only"),
            @ApiResponse(responseCode = "409", description = "Scraper already running")
    })
    @GetMapping("/run")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> runPythonScript() {
        return scraperJobService.start(OfferSource.NOFLUFF, ScraperJobService.Mode.SCRAPE_AND_IMPORT, "manual")
                .map(run -> ResponseEntity.accepted().body(Map.of(
                        "message", "Skrypt uruchomiony w tle. Oferty zostaną zaimportowane automatycznie po jego zakończeniu.",
                        "jobId", run.jobId())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Scraper No Fluff Jobs już pracuje — poczekaj na zakończenie.")));
    }

    @Operation(summary = "Import offers from JSON body",
               description = "Accepts a JSON array of offer objects for one portal and imports new ones into the database. " +
                             "Every offer must belong to the chosen portal (its 'source' and offerUrl domain); others are rejected. " +
                             "Existing offers (matched by offerUrl) are skipped. Each object must contain at minimum 'offerUrl' and 'company'. " +
                             "Supported fields: title, description, location, salaryRange, level, offerUrl, company, fetchedAt, requirements, niceToHave, source.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Import summary returned (imported / skipped / rejected / errors count)"),
            @ApiResponse(responseCode = "400", description = "Empty payload or unknown source"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importOffers(
            @RequestBody List<Map<String, Object>> payload,
            @Parameter(description = "Portal: NoFluff (default), TheProtocol or JustJoin")
            @RequestParam(defaultValue = "NoFluff") String source) {
        if (payload == null || payload.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Pusta lista — prześlij tablicę obiektów JSON."));
        }
        OfferSource portal = OfferSource.fromParam(source).orElse(OfferSource.NOFLUFF);
        return ResponseEntity.ok(offerImportService.importFromPayload(payload, portal).toMap());
    }

    @Operation(summary = "Import offer from URL",
            description = "Scrapes a job offer from the given URL and saves it to the database. " +
                    "The scraper is chosen by the URL domain (nofluffjobs.com, theprotocol.it or justjoin.it); other domains are rejected. " +
                    "userId is resolved automatically from the JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Offer imported successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid/unsupported URL or scraping error"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "409", description = "Offer already exists"),
            @ApiResponse(responseCode = "423", description = "Scraping disabled for this portal")
    })
    @PostMapping("/import-from-url")
    public ResponseEntity<String> importOfferFromUrl(
            @RequestParam String offerUrl,
            @AuthenticationPrincipal UserDetails principal   // ← userId z JWT, nie z RequestParam
    ) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Not authenticated — please send a valid Bearer token");
        }

        // Pobierz encję usera po username z tokenu JWT
        User user = userRepository.findByUsername(principal.getUsername())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not found for username: " + principal.getUsername());
        }

        try {
            OfferDTO importedOffer = offerFacade.addOfferFromUrl(user.getId(), offerUrl);
            return ResponseEntity.ok("Offer successfully imported: " + importedOffer.getTitle());
        } catch (IllegalStateException e) {
            logger.warn("Duplicate offer detected: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ScraperJobService.ScraperDisabledException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error importing offer from URL: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error importing offer: " + e.getMessage());
        }
    }
}
