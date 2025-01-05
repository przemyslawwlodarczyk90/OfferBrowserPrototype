package com.example.offerbrowserprototype.domain.statistics;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CityDistributionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CityDistributionHandler.class);
    private final OfferRepository offerRepository;

    public Map<String, Long> getCityDistribution() {
        logger.info("Fetching city distribution...");

        try {
            List<CityDistribution> rawDistribution = offerRepository.getCityDistributionSimple();
            logger.debug("Raw city distribution result: {}", rawDistribution);

            Map<String, Long> processedDistribution = new HashMap<>();
            rawDistribution.forEach(cityDist -> {
                String city = cityDist.getId().split(",")[0].trim();
                processedDistribution.merge(city, cityDist.getCount(), Long::sum);
            });

            logger.debug("Processed city distribution: {}", processedDistribution);
            return processedDistribution;
        } catch (Exception e) {
            logger.error("Error fetching city distribution", e);
            throw e;
        }
    }
}
