package com.example.offerbrowserprototype.domain.statistics;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class LevelDistributionHandler {

    private final OfferRepository offerRepository;

    public Map<String, Double> getLevelDistribution() {
        long totalOffers = offerRepository.countByIsDuplicateFalse();
        if (totalOffers == 0) {
            return Map.of(); // Empty map if no offers exist
        }

        Map<String, Long> levelCounts = offerRepository.countByLevelAndIsDuplicateFalse();
        return levelCounts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> (entry.getValue() * 100.0) / totalOffers));
    }
}