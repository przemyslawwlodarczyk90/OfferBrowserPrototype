package com.example.offerbrowserprototype.domain.offer;

import org.springframework.stereotype.Component;

@Component
class OfferDeletionHandler {

    private final OfferRepository offerRepository;

    public OfferDeletionHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    void deleteOffer(String id) {
        offerRepository.deleteById(id);
    }
}
