package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Arrays;

@Component
class OfferRetrievalHandler {

    OfferDto getOffer(Long id) {
        return new OfferDto(id, "Title", "Description", "Location");
    }

    List<OfferDto> getAllOffers() {
        return Arrays.asList(new OfferDto(1L, "Title1", "Description1", "Location1"),
                new OfferDto(2L, "Title2", "Description2", "Location2"));
    }
}