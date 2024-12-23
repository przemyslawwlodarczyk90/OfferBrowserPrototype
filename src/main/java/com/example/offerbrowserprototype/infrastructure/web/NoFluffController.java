package com.example.offerbrowserprototype.infrastructure.web;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/python-script")
@Tag(name = "Python Script", description = "Operations related to Python script execution and offer imports")
public class NoFluffController {

    private static final Logger logger = LoggerFactory.getLogger(NoFluffController.class);
    private final PythonScriptService scriptService;
    private final OfferImportService offerImportService;

    public NoFluffController(PythonScriptService scriptService, OfferImportService offerImportService) {
        this.scriptService = scriptService;
        this.offerImportService = offerImportService;
    }

    @Operation(summary = "Run Python script", description = "Executes the Python script for scraping job offers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Script executed successfully"),
            @ApiResponse(responseCode = "500", description = "Error during script execution")
    })
    @GetMapping("/run")
    public ResponseEntity<String> runPythonScript() {
        String result = scriptService.runScript();

        // Trigger import automatically after script execution
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
}
