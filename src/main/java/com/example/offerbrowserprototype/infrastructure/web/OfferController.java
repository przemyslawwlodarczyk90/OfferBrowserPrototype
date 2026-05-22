package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.facade.OfferFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@Tag(name = "Offers", description = "Operations related to job offers")
@PreAuthorize("isAuthenticated()")
public class OfferController {

    private final OfferFacade offerFacade;

    public OfferController(OfferFacade offerFacade) {
        this.offerFacade = offerFacade;
    }

    @PostMapping
    @Operation(summary = "Add offer", description = "Creates a new job offer in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Offer created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<OfferDTO> addOffer(@Valid @RequestBody OfferDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(offerFacade.addOffer(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update offer", description = "Updates an existing job offer by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Offer updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    public ResponseEntity<OfferDTO> updateOffer(
            @PathVariable Long id,
            @Valid @RequestBody OfferDTO dto
    ) {
        return ResponseEntity.ok(offerFacade.updateOffer(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete offer", description = "Permanently removes a job offer from the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Offer deleted"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        offerFacade.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get offer by ID", description = "Returns a single job offer by its database ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Offer found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    public ResponseEntity<OfferDTO> getOffer(@PathVariable Long id) {
        return ResponseEntity.ok(offerFacade.getOffer(id));
    }

    @GetMapping
    @Operation(summary = "Get all offers", description = "Returns all job offers including duplicates.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<List<OfferDTO>> getAllOffers() {
        return ResponseEntity.ok(offerFacade.getAllOffers());
    }

    @PostMapping("/{offerId}/mark-duplicate")
    @Operation(summary = "Mark offer as duplicate by ID", description = "Sets the global duplicate flag on the offer. Admin-only action.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Offer marked as duplicate"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    public ResponseEntity<Void> markDuplicate(@PathVariable Long offerId) {
        offerFacade.markAsDuplicateById(offerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mark-duplicate-by-url")
    @Operation(summary = "Mark offer as duplicate by URL", description = "Sets the global duplicate flag on the offer matching the given URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Offer marked as duplicate"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Offer not found for given URL")
    })
    public ResponseEntity<Void> markDuplicateByUrl(@RequestParam String offerUrl) {
        offerFacade.markAsDuplicateByUrl(offerUrl);
        return ResponseEntity.ok().build();
    }
}
