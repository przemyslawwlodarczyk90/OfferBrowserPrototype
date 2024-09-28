package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.offer.OfferFacade;
import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
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

    public OfferController(OfferFacade offerFacade) {
        this.offerFacade = offerFacade;
    }

    @Operation(summary = "Add a new offer", description = "Creates a new job offer in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Offer created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OfferDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<OfferDTO> addOffer(@RequestBody OfferDTO offerDto) {
        OfferDTO createdOffer = offerFacade.addOffer(offerDto);
        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED); // Kod 201 jeśli oferta została dodana
    }

    @Operation(summary = "Update an existing offer", description = "Updates an existing job offer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OfferDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Offer not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable String id, @RequestBody OfferDTO offerDto) {
        OfferDTO updatedOffer = offerFacade.updateOffer(id, offerDto);
        return new ResponseEntity<>(updatedOffer, HttpStatus.OK); // Kod 200 jeśli oferta została zaktualizowana
    }
    @Operation(summary = "Delete an offer", description = "Deletes a job offer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Offer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Offer not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable String id) {
        offerFacade.deleteOffer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Kod 204 jeśli oferta została usunięta
    }

    @Operation(summary = "Get a specific offer", description = "Retrieves a specific job offer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OfferDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Offer not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOffer(@PathVariable String id) {
        OfferDTO offer = offerFacade.getOffer(id);
        if (offer != null) {
            return new ResponseEntity<>(offer, HttpStatus.OK); // Kod 200 jeśli oferta została znaleziona
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Kod 404 jeśli oferta nie istnieje
        }
    }
    @Operation(summary = "Get all offers", description = "Retrieves all job offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offers retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OfferDTO.class))})
    })
    @GetMapping
    public ResponseEntity<List<OfferDTO>> getAllOffers() {
        List<OfferDTO> offers = offerFacade.getAllOffers();
        return new ResponseEntity<>(offers, HttpStatus.OK); // Kod 200 z listą wszystkich ofert
    }
}
