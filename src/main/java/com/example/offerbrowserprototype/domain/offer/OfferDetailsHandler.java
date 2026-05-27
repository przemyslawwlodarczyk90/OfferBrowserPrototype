package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OfferDetailsHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public OfferDetailsHandler(
            OfferRepository offerRepository,
            OfferMapper offerMapper
    ) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    @Transactional(readOnly = true)
    public OfferDTO getOfferById(Long id) {
        return offerRepository.findById(id)
                .map(offerMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));
    }
}
