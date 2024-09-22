package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.stereotype.Component;

@Component
class OfferAdditionHandler {

    OfferDTO addOffer(OfferDTO offerDto) {
        offerDto.setId(1L);
        return offerDto;
    }
}