package com.example.offerbrowserprototype.domain.statistics;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CityDistributionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CityDistributionHandler.class);
    private final OfferRepository offerRepository;

    public CityDistributionHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public Map<String, Long> getCityDistribution() {
        return getCityDistribution(null);
    }

    public Map<String, Long> getCityDistribution(OfferSource source) {
        logger.info("Fetching city distribution (source={})...", source);

        try {
            List<CityDistributionProjection> rawDistribution = source == null
                    ? offerRepository.getCityDistributionSimple()
                    : offerRepository.getCityDistributionBySource(source.value());

            logger.debug("Raw city distribution result: {}", rawDistribution);

            Map<String, Long> processedDistribution = new HashMap<>();

            for (CityDistributionProjection cityDist : rawDistribution) {
                if (cityDist.getId() == null) {
                    continue;
                }

                // np. "Warszawa, Mazowieckie" → "Warszawa"
                String city = cityDist.getId().split(",")[0].trim();
                processedDistribution.merge(city, cityDist.getCount(), Long::sum);
            }

            logger.debug("Processed city distribution: {}", processedDistribution);
            return processedDistribution;

        } catch (Exception e) {
            logger.error("Error fetching city distribution", e);
            throw e;
        }
    }
}
