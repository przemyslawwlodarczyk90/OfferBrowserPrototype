package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.domain.statistics.CityDistributionHandler;
import com.example.offerbrowserprototype.domain.statistics.LevelBySourceDistributionHandler;
import com.example.offerbrowserprototype.domain.statistics.LevelDistributionHandler;
import com.example.offerbrowserprototype.domain.statistics.OfferCountHandler;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Map;

/** Statistics per portal; {@code source == null} means all portals. Cache keys include the source. */
@Component
public class StatisticsFacade {

    private final OfferCountHandler offerCountHandler;
    private final LevelDistributionHandler levelDistributionHandler;
    private final CityDistributionHandler cityDistributionHandler;
    private final LevelBySourceDistributionHandler levelBySourceHandler;

    public StatisticsFacade(OfferCountHandler offerCountHandler, LevelDistributionHandler levelDistributionHandler,
                            CityDistributionHandler cityDistributionHandler,
                            LevelBySourceDistributionHandler levelBySourceHandler) {
        this.offerCountHandler = offerCountHandler;
        this.levelDistributionHandler = levelDistributionHandler;
        this.cityDistributionHandler = cityDistributionHandler;
        this.levelBySourceHandler = levelBySourceHandler;
    }

    public long getTotalOffers() {
        return offerCountHandler.getTotalOffers();
    }

    public Map<String, Long> getLevelDistribution() {
        return levelDistributionHandler.getLevelDistribution();
    }

    public Map<String, Long> getCityDistribution() {
        return cityDistributionHandler.getCityDistribution();
    }

    @Cacheable(value = "totalOffers", key = "#source == null ? 'ALL' : #source.value()", unless = "#result == 0")
    public long getTotalOffers(OfferSource source) {
        return offerCountHandler.getTotalOffers(source);
    }

    @Cacheable(value = "levelDistribution", key = "#source == null ? 'ALL' : #source.value()", unless = "#result.isEmpty()")
    public Map<String, Long> getLevelDistribution(OfferSource source) {
        return levelDistributionHandler.getLevelDistribution(source);
    }

    @Cacheable(value = "cityDistribution", key = "#source == null ? 'ALL' : #source.value()", unless = "#result.isEmpty()")
    public Map<String, Long> getCityDistribution(OfferSource source) {
        return cityDistributionHandler.getCityDistribution(source);
    }

    /** All portals: level → (portal → offers). */
    @Cacheable(value = "levelDistributionBySource", key = "'ALL'", unless = "#result.isEmpty()")
    public Map<String, Map<String, Long>> getLevelDistributionBySource() {
        return levelBySourceHandler.getDistribution();
    }
}
