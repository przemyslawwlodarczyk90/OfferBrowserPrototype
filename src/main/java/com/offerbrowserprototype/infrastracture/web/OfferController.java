package com.offerbrowserprototype.infrastracture.web;

import com.offerbrowserprototype.domain.offer.OfferFacade;
import com.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    private final OfferFacade offerFacade;

    public OfferController(OfferFacade offerFacade) {
        this.offerFacade = offerFacade;
    }

    @PostMapping
    public ResponseEntity<OfferDTO> addOffer(@RequestBody OfferDTO offerDTO) {
        OfferDTO createdOffer = offerFacade.addOffer(offerDTO);
        return ResponseEntity.ok(createdOffer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable Long id, @RequestBody OfferDTO offerDTO) {
        OfferDTO updatedOffer = offerFacade.updateOffer(id, offerDTO);
        return ResponseEntity.ok(updatedOffer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        offerFacade.deleteOffer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOffer(@PathVariable Long id) {
        OfferDTO offer = offerFacade.getOffer(id);
        return ResponseEntity.ok(offer);
    }

    @GetMapping
    public ResponseEntity<List<OfferDTO>> getAllOffers() {
        List<OfferDTO> offers = offerFacade.getAllOffers();
        return ResponseEntity.ok(offers);
    }
}