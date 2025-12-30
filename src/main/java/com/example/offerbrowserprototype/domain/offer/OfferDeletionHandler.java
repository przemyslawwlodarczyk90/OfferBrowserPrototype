package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OfferDeletionHandler {

    private final OfferRepository offerRepository;

    public OfferDeletionHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public void deleteOffer(String id) {
        offerRepository.deleteById(UUID.fromString(id));
    }
}
