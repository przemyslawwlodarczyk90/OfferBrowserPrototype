package com.example.offerbrowserprototype.domain.statistics;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.domain.offer.OfferSource;

import org.springframework.stereotype.Component;

@Component

public class OfferCountHandler {

    private final OfferRepository offerRepository;

    public OfferCountHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public long getTotalOffers() {
        return getTotalOffers(null);
    }

    public long getTotalOffers(OfferSource source) {
        return source == null
                ? offerRepository.countNonDuplicate()
                : offerRepository.countNonDuplicateBySource(source.value());
    }

}