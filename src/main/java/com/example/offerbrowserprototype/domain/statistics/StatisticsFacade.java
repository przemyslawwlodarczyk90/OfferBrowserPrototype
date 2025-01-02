package com.example.offerbrowserprototype.domain.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StatisticsFacade {

    private final OfferCountHandler offerCountHandler;
    private final LevelDistributionHandler levelDistributionHandler;
    private final CityDistributionHandler cityDistributionHandler;

    public long getTotalOffers() {
        return offerCountHandler.getTotalOffers();
    }

    public Map<String, Double> getLevelDistribution() {
        return levelDistributionHandler.getLevelDistribution();
    }

    public Map<String, Long> getCityDistribution() {
        return cityDistributionHandler.getCityDistribution();
    }

    public StatisticsResponse getMainStatistics() {
        long totalOffers = getTotalOffers();
        Map<String, Double> levelDistribution = getLevelDistribution();
        Map<String, Long> cityDistribution = getCityDistribution();

        return new StatisticsResponse(totalOffers, levelDistribution, cityDistribution);
    }
}
