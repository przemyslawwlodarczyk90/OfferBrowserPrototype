package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDTO;
import com.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

@Component
class OfferAdditionHandler {


    private final OfferRepository offerRepository;

    public OfferAdditionHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    OfferDTO addOffer(OfferDTO offerDto) {
        Offer offer = OfferMapper.mapToEntity(offerDto);
        Offer savedOffer = offerRepository.save(offer);
        return OfferMapper.mapToDto(savedOffer);
    }
}