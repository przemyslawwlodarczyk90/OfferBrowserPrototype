package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.offer.FlagType;
import com.example.offerbrowserprototype.domain.offer.OfferFlagHandler;
import org.springframework.stereotype.Component;

@Component
public class UserOfferMarkUselessHandler {

    private final OfferFlagHandler offerFlagHandler;

    public UserOfferMarkUselessHandler(OfferFlagHandler offerFlagHandler) {
        this.offerFlagHandler = offerFlagHandler;
    }

    public void markAsUseless(Long userId, Long offerId) {
        offerFlagHandler.flag(userId, offerId, FlagType.USELESS);
    }
}
