package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarkAsDuplicateHandler {

    private final OfferRepository offerRepository;


    public void handleById(String offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer with ID " + offerId + " not found."));
        markAsDuplicate(offer);
    }

    public void handleByUrl(String offerUrl) {
        Offer offer = offerRepository.findByOfferUrl(offerUrl)
                .orElseThrow(() -> new IllegalArgumentException("Offer with URL " + offerUrl + " not found."));
        markAsDuplicate(offer);
    }

    private void markAsDuplicate(Offer offer) {
        offer.setDuplicate(true);
        offerRepository.save(offer);
    }
}
