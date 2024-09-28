package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.stereotype.Component;

@Component
class OfferAdditionHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public OfferAdditionHandler(OfferRepository offerRepository, OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    OfferDTO addOffer(OfferDTO offerDto) {
        Offer offer = offerMapper.toEntity(offerDto);
        Offer savedOffer = offerRepository.save(offer); // Zapisanie w bazie danych
        return offerMapper.toDTO(savedOffer); // Zwrócenie zaktualizowanego obiektu DTO
    }
}
