package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.facade.OfferFacade;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
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
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/python-script")
@Tag(name = "No Fluff Controller", description = "Operations for downloading and writing to the database of offers from the No Fluff Jobs portal.")
@PreAuthorize("isAuthenticated()")
public class NoFluffController {

    private static final Logger logger = LoggerFactory.getLogger(NoFluffController.class);
    private final PythonScriptService scriptService;
    private final OfferImportService offerImportService;
    private final OfferFacade offerFacade;
    private final OfferRepository offerRepository;

    public NoFluffController(PythonScriptService scriptService, OfferImportService offerImportService, OfferFacade offerFacade, OfferRepository offerRepository) {
        this.scriptService = scriptService;
        this.offerImportService = offerImportService;
        this.offerFacade = offerFacade;
        this.offerRepository = offerRepository;
    }

    @Operation(summary = "Run Python script", description = "Executes the Python script for scraping job offers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Script executed successfully"),
            @ApiResponse(responseCode = "500", description = "Error during script execution")
    })
    @GetMapping("/run")
    public ResponseEntity<String> runPythonScript() {
        String result = scriptService.runScript();


        CompletableFuture.runAsync(() -> {
            try {
                logger.info("Starting automatic offer import...");
                offerImportService.importOffersFromJson("data/offers/detailed_offers.json");
                logger.info("Automatic offer import completed successfully.");
            } catch (Exception e) {
                logger.error("Error during automatic offer import: {}", e.getMessage());
            }
        });

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Import offers from JSON", description = "Imports job offers from a JSON file into the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offers imported successfully"),
            @ApiResponse(responseCode = "500", description = "Error during offer import")
    })
    @PostMapping("/import")
    public ResponseEntity<String> importOffers(@RequestParam(defaultValue = "data/offers/detailed_offers.json") String filePath) {
        try {
            offerImportService.importOffersFromJson(filePath);
            return ResponseEntity.ok("Offers imported successfully from " + filePath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during offer import: " + e.getMessage());
        }
    }

//    @Operation(summary = "Import offer from URL", description = "Scrapes a job offer from the given URL and saves it to the database.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Offer imported successfully"),
//            @ApiResponse(responseCode = "400", description = "Invalid URL or scraping error")
//    })
//    @PostMapping("/import-from-url")
//    public ResponseEntity<String> importOfferFromUrl(@RequestParam String offerUrl) {
//        try {
//            OfferDTO importedOffer = offerFacade.addOfferFromUrl(offerUrl);
//            return ResponseEntity.ok("Offer successfully imported from URL.");
//        } catch (IllegalStateException e) {
//            logger.warn("Duplicate offer detected: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        } catch (Exception e) {
//            logger.error("Error importing offer from URL: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error importing offer: " + e.getMessage());
//        }
//    }

}