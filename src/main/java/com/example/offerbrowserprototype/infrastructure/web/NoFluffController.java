package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.infrastructure.service.OfferImportService;
import com.example.offerbrowserprototype.infrastructure.service.PythonScriptService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/python-script")
@Tag(name = "Python Script", description = "Operations related to Python script execution and offer imports")
public class NoFluffController {

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
