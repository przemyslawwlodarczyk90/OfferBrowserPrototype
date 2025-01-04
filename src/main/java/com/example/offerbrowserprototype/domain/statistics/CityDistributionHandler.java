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
        // Pobranie danych z repozytorium
        Map<String, Long> rawCityDistribution = offerRepository.getCityDistribution();
        System.out.println("Raw city distribution from MongoDB: " + rawCityDistribution);

        // Zwracamy dane bez zmian
        return rawCityDistribution;
    }
}
