package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.facade.OfferFacade;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Add offer")
    public ResponseEntity<OfferDTO> addOffer(@Valid @RequestBody OfferDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(offerFacade.addOffer(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update offer")
    public ResponseEntity<OfferDTO> updateOffer(
            @PathVariable Long id,
            @Valid @RequestBody OfferDTO dto
    ) {
        return ResponseEntity.ok(offerFacade.updateOffer(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete offer")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        offerFacade.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get offer by ID")
    public ResponseEntity<OfferDTO> getOffer(@PathVariable Long id) {
        return ResponseEntity.ok(offerFacade.getOffer(id));
    }

    @GetMapping
    @Operation(summary = "Get all offers")
    public ResponseEntity<List<OfferDTO>> getAllOffers() {
        return ResponseEntity.ok(offerFacade.getAllOffers());
    }

    @PostMapping("/{offerId}/push/{provider}")
    @Operation(summary = "Push offer to provider")
    public ResponseEntity<Void> pushOffer(
            @PathVariable Long offerId,
            @PathVariable String provider
    ) {
        offerFacade.pushOfferToProvider(offerId, provider);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{offerId}/mark-duplicate")
    @Operation(summary = "Mark offer as duplicate by ID")
    public ResponseEntity<Void> markDuplicate(@PathVariable Long offerId) {
        offerFacade.markAsDuplicateById(offerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mark-duplicate-by-url")
    @Operation(summary = "Mark offer as duplicate by URL")
    public ResponseEntity<Void> markDuplicateByUrl(@RequestParam String offerUrl) {
        offerFacade.markAsDuplicateByUrl(offerUrl);
        return ResponseEntity.ok().build();
    }
}
