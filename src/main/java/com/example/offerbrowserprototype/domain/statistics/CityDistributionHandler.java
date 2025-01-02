package com.example.offerbrowserprototype.domain.statistics;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
class CityDistributionHandler {

    private final OfferRepository offerRepository;

    public Map<String, Long> getCityDistribution() {
        return offerRepository.countByCityAndIsDuplicateFalse();
    }
}