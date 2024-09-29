package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.offer.OfferFacade;
import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@Tag(name = "Offers", description = "Operations related to job offers")
public class OfferController {

    private final OfferFacade offerFacade;
    private final OfferService offerService;

    public OfferController(OfferFacade offerFacade, OfferService offerService) {
        this.offerFacade = offerFacade;
        this.offerService = offerService;
    }

    @Operation(summary = "Add a new offer", description = "Creates a new job offer in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Offer created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<OfferDTO> addOffer(@RequestBody OfferDTO offerDto) {
        OfferDTO createdOffer = offerFacade.addOffer(offerDto);
        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing offer", description = "Updates an existing job offer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDTO.class))),
            @ApiResponse(responseCode = "404", description = "Offer not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable String id, @RequestBody OfferDTO offerDto) {
        OfferDTO updatedOffer = offerFacade.updateOffer(id, offerDto);
        return new ResponseEntity<>(updatedOffer, HttpStatus.OK);
    }

    @Operation(summary = "Delete an offer", description = "Deletes a job offer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Offer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Offer not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable String id) {
        offerFacade.deleteOffer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping
    public ResponseEntity<List<OfferDTO>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @Operation(summary = "Get not applied offers", description = "Retrieve all offers that haven't been applied to.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved not applied offers")
    })
    @GetMapping("/not-applied")
    public ResponseEntity<List<OfferDTO>> getNotAppliedOffers() {
        return ResponseEntity.ok(offerService.getNotAppliedOffers());
    }

    @Operation(summary = "Get applied offers", description = "Retrieve all offers that have been applied to.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved applied offers")
    })
    @GetMapping("/applied")
    public ResponseEntity<List<OfferDTO>> getAppliedOffers() {
        return ResponseEntity.ok(offerService.getAppliedOffers());
    }

    @Operation(summary = "Apply to an offer", description = "Mark an offer as applied.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully applied to the offer")
    })
    @PostMapping("/{offerId}/apply")
    public ResponseEntity<Void> applyToOffer(@PathVariable String offerId) {
        offerService.applyToOffer(offerId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get offer details", description = "Retrieve the details of an offer by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved offer details"),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    @GetMapping("/{offerId}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable String offerId) {
        return ResponseEntity.ok(offerService.getOfferById(offerId));
    }
}