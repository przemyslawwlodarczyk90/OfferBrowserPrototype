package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

@Component
class OfferDetailsHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public OfferDetailsHandler(OfferRepository offerRepository, OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    // Pobieranie szczegółów oferty na podstawie ID
    public OfferDTO getOfferById(String offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));
        return offerMapper.toDTO(offer);
    }
}
