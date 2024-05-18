package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Arrays;

@Component
class OfferRetrievalHandler {

    OfferDTO getOffer(Long id) {

        return new OfferDTO();
    }

    List<OfferDTO> getAllOffers() {
        // Przykładowe dane dla demonstracji
        return Arrays.asList(
                new OfferDTO(),
                new OfferDTO(),
                new OfferDTO()
        );
    }
}