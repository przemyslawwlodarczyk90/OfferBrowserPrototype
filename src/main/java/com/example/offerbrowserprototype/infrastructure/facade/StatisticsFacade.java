package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.statistics.CityDistributionHandler;
import com.example.offerbrowserprototype.domain.statistics.LevelDistributionHandler;
import com.example.offerbrowserprototype.domain.statistics.OfferCountHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StatisticsFacade {

    private final OfferCountHandler offerCountHandler;
    private final LevelDistributionHandler levelDistributionHandler;
    private final CityDistributionHandler cityDistributionHandler;

    public StatisticsFacade(OfferCountHandler offerCountHandler, LevelDistributionHandler levelDistributionHandler, CityDistributionHandler cityDistributionHandler) {
        this.offerCountHandler = offerCountHandler;
        this.levelDistributionHandler = levelDistributionHandler;
        this.cityDistributionHandler = cityDistributionHandler;
    }

    @Cacheable(value = "totalOffers", unless = "#result == 0")
    public long getTotalOffers() {
        return offerCountHandler.getTotalOffers();
    }



    @Cacheable(value = "levelDistribution", unless = "#result.isEmpty()")
    public Map<String, Long> getLevelDistribution() {
        return levelDistributionHandler.getLevelDistribution();
    }

    @Cacheable(value = "cityDistribution", unless = "#result.isEmpty()")
    public Map<String, Long> getCityDistribution() {
        return cityDistributionHandler.getCityDistribution();
    }
}
