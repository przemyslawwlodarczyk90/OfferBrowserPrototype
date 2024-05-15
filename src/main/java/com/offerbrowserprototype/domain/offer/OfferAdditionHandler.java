package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.stereotype.Component;

@Component
class OfferAdditionHandler {

    OfferDTO addOffer(OfferDTO offerDto) {
        offerDto.setId(1L);
        return offerDto;
    }
}