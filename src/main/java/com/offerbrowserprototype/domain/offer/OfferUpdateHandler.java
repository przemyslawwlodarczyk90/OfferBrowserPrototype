package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDto;
import org.springframework.stereotype.Component;

@Component
class OfferUpdateHandler {

    OfferDto updateOffer(Long id, OfferDto offerDto) {
        offerDto.setId(id);
        return offerDto;
    }
}