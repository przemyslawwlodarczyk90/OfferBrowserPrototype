package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.stereotype.Component;

@Component
class OfferUpdateHandler {

    OfferDTO updateOffer(Long id, OfferDTO offerDto) {
        offerDto.setId(id);
        return offerDto;
    }
}