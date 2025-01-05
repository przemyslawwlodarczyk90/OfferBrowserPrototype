package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

@Component
public class OfferApplicationHandler {

    private final OfferRepository offerRepository;
    private final OfferDetailsHandler offerDetailsHandler;
    private final ApplicationNoteHandler applicationNoteHandler;

    public OfferApplicationHandler(OfferRepository offerRepository,
                                   OfferDetailsHandler offerDetailsHandler,
                                   ApplicationNoteHandler applicationNoteHandler) {
        this.offerRepository = offerRepository;
        this.offerDetailsHandler = offerDetailsHandler;
        this.applicationNoteHandler = applicationNoteHandler;
    }

    public void applyToOfferWithNote(String offerId) {
        OfferDTO offer = offerDetailsHandler.getOfferById(offerId);

        if (offer == null) {
            throw new IllegalArgumentException("Offer not found for ID: " + offerId);
        }

        if (offer.getOfferUrl() == null || offer.getCompany() == null) {
            throw new IllegalArgumentException("Offer details are incomplete. Cannot save application note.");
        }


        Offer entity = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));
        entity.setApplied(true);
        offerRepository.save(entity);


        applicationNoteHandler.saveApplicationNote(offerId, offer.getOfferUrl(), offer.getCompany());
    }
}

