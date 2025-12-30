package com.example.offerbrowserprototype.domain.statistics;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferCountHandler {

    private final OfferRepository offerRepository;

    public OfferCountHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public long getTotalOffers() {
        return offerRepository.countByIsDuplicateFalse();
    }
    
}