package com.example.offerbrowserprototype.domain.offer;

import org.springframework.stereotype.Component;

@Component
public class MarkAsDuplicateHandler {

    private final OfferFlagHandler offerFlagHandler;

    public MarkAsDuplicateHandler(OfferFlagHandler offerFlagHandler) {
        this.offerFlagHandler = offerFlagHandler;
    }

    public void handleById(Long userId, Long offerId) {
        offerFlagHandler.flag(userId, offerId, FlagType.DUPLICATE);
    }

    public void handleByUrl(Long userId, String offerUrl) {
        offerFlagHandler.flagByUrl(userId, offerUrl, FlagType.DUPLICATE);
    }
}
