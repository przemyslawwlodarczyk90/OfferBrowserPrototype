package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.facade.OfferFacade;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import com.example.offerbrowserprototype.infrastructure.service.OfferImportService;
import com.example.offerbrowserprototype.infrastructure.service.PythonScriptService;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/python-script")
@Tag(name = "Import & Scripts", description = "Endpoints for importing job offers: run the scraper script, import from JSON body, or import a single offer from a URL.")
@PreAuthorize("isAuthenticated()")
public class NoFluffController {

    private static final Logger logger = LoggerFactory.getLogger(NoFluffController.class);

    private final PythonScriptService scriptService;
    private final OfferImportService  offerImportService;
    private final OfferFacade         offerFacade;
    private final OfferRepository     offerRepository;
    private final UserRepository      userRepository;

    public NoFluffController(PythonScriptService scriptService,
                             OfferImportService offerImportService,
                             OfferFacade offerFacade,
                             OfferRepository offerRepository,
                             UserRepository userRepository) {
        this.scriptService    = scriptService;
        this.offerImportService = offerImportService;
        this.offerFacade      = offerFacade;
        this.offerRepository  = offerRepository;
        this.userRepository   = userRepository;
    }

    @Operation(summary = "Run Python script")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Script executed successfully"),
            @ApiResponse(responseCode = "500", description = "Error during script execution")
    })
    @GetMapping("/run")
    public ResponseEntity<Map<String, String>> runPythonScript() {
        CompletableFuture.runAsync(() -> {
            try {
                logger.info("Python script started in background...");
                String output = scriptService.runScript();
                logger.info("Python script completed. Output: {}", output);
                offerImportService.importOffersFromJson("data/offers/detailed_offers.json");
                logger.info("Offer import completed successfully.");
            } catch (Exception e) {
                logger.error("Error during script execution or import: {}", e.getMessage());
            }
        });

        return ResponseEntity.accepted()
                .body(Map.of("message", "Skrypt uruchomiony w tle. Oferty zostaną zaimportowane automatycznie po jego zakończeniu."));
    }

    @Operation(summary = "Import offers from JSON body")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Offers imported successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid JSON structure"),
            @ApiResponse(responseCode = "500", description = "Error during offer import")
    })
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importOffers(@RequestBody List<Map<String, Object>> payload) {
        if (payload == null || payload.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Pusta lista — prześlij tablicę obiektów JSON."));
        }

        int imported = 0, skipped = 0, errors = 0;
        for (Map<String, Object> item : payload) {
            try {
                if (!item.containsKey("offerUrl") || item.get("offerUrl") == null) {
                    errors++;
                    logger.warn("Skipping offer — missing required field 'offerUrl': {}", item);
                    continue;
                }
                if (!item.containsKey("company") || item.get("company") == null) {
                    errors++;
                    logger.warn("Skipping offer — missing required field 'company': {}", item.get("offerUrl"));
                    continue;
                }

                com.example.offerbrowserprototype.domain.offer.Offer offer =
                        offerRepository.findByOfferUrl(item.get("offerUrl").toString()).orElse(null);

                if (offer != null) { skipped++; continue; }

                offer = new com.example.offerbrowserprototype.domain.offer.Offer();
                String rawTitle = str(item, "title");
                offer.setTitle(rawTitle != null ? rawTitle.replaceAll("(?i)\\s*NOWA\\s*$", "").trim() : null);
                offer.setDescription(str(item, "description"));
                offer.setLocation(str(item, "location"));
                offer.setSalaryRange(str(item, "salaryRange"));
                offer.setLevel(str(item, "level"));
                offer.setOfferUrl(item.get("offerUrl").toString());
                offer.setCompany(item.get("company").toString());
                offer.setDuplicate(false);
                offer.setFetchedAt(java.time.LocalDateTime.now());

                offerRepository.save(offer);
                imported++;
            } catch (Exception e) {
                errors++;
                logger.error("Error processing offer: {}", e.getMessage());
            }
        }

        return ResponseEntity.ok(Map.of(
                "imported",  imported,
                "skipped",   skipped,
                "errors",    errors,
                "total",     payload.size()
        ));
    }

    private static String str(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v != null ? v.toString() : null;
    }

    @Operation(summary = "Import offer from URL",
            description = "Scrapes a job offer from the given URL and saves it to the database. " +
                    "userId is resolved automatically from the JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Offer imported successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid URL or scraping error"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "409", description = "Offer already exists")
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
        } catch (Exception e) {
            logger.error("Error importing offer from URL: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error importing offer: " + e.getMessage());
        }
    }
}