package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDto;
import org.springframework.stereotype.Component;

@Component
class OfferAdditionHandler {

    OfferDto addOffer(OfferDto offerDto) {
        offerDto.setId(1L);
        return offerDto;
    }
}