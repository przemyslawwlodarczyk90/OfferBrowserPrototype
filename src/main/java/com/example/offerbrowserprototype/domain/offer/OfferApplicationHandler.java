package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

@Component
class OfferApplicationHandler {

    private final OfferRepository offerRepository;

    public OfferApplicationHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public void applyToOffer(String offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));

        offer.setApplied(true);
        offerRepository.save(offer);
    }
}
