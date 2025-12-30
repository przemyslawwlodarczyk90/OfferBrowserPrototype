package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

@Component
public class MarkAsDuplicateHandler {

    private final OfferRepository offerRepository;

    public MarkAsDuplicateHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public void handleById(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));
        mark(offer);
    }

    public void handleByUrl(String offerUrl) {
        Offer offer = offerRepository.findByOfferUrl(offerUrl)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));
        mark(offer);
    }

    private void mark(Offer offer) {
        offer.setDuplicate(true);
        offerRepository.save(offer);
    }
}
