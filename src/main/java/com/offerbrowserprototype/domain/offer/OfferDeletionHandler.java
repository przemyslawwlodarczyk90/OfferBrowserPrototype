package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

@Component
class OfferDeletionHandler {


    private final OfferRepository offerRepository;

    public OfferDeletionHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }
}