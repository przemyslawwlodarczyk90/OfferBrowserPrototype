package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.infrastructure.service.ExternalJobOfferService;
import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/api/external-offers")
@Tag(name = "External Job Offers", description = "Operations related to fetching job offers from external providers")
public class ExternalOfferController {

    private final ExternalJobOfferService externalJobOfferService;

    public ExternalOfferController(ExternalJobOfferService externalJobOfferService) {
        this.externalJobOfferService = externalJobOfferService;
    }

    @Operation(summary = "Fetch all external job offers", description = "Retrieves job offers from all available external providers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of offers", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDTO.class))),
            @ApiResponse(responseCode = "204", description = "No content found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<OfferDTO>> getAllExternalOffers() {
        List<OfferDTO> offers = externalJobOfferService.fetchExternalOffers();
        return ResponseEntity.ok(offers);
    }

    @Operation(summary = "Get available external providers", description = "Retrieves a list of all currently available external job offer providers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of providers", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/providers")
    public ResponseEntity<List<String>> getAvailableProviders() {
        List<String> providers = externalJobOfferService.getProviderNames();
        return ResponseEntity.ok(providers);
    }

    // Example endpoint with validation
    @Operation(summary = "Get offers with pagination", description = "Fetch job offers with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated list of offers", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content)
    })
    @GetMapping("/paged")
    public ResponseEntity<List<OfferDTO>> getPagedExternalOffers(
            @RequestParam @Valid @Min(1) int page,
            @RequestParam @Valid @Min(1) int size,
            @RequestParam(required = false) @NotBlank String provider) {
        // Your logic to fetch paginated offers with optional provider filtering
        List<OfferDTO> offers = externalJobOfferService.fetchExternalOffers(); // Replace with actual logic
        return ResponseEntity.ok(offers);
    }
}
