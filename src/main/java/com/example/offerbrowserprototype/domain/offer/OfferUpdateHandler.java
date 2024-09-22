package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.stereotype.Component;

@Component
class OfferUpdateHandler {

    OfferDTO updateOffer(Long id, OfferDTO offerDto) {
        offerDto.setId(id);
        return offerDto;
    }
}