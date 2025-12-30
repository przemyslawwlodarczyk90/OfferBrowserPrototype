package com.example.offerbrowserprototype.domain.statistics;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component

public class LevelDistributionHandler {

    private static final Logger logger = LoggerFactory.getLogger(LevelDistributionHandler.class);
    private final OfferRepository offerRepository;

    public LevelDistributionHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public Map<String, Long> getLevelDistribution() {
        logger.info("Fetching level distribution...");
        try {
            var rawDistribution = offerRepository.getLevelDistributionSimple();
            logger.debug("Raw aggregation result: {}", rawDistribution);

            return rawDistribution.stream()
                    .collect(Collectors.toMap(
                            LevelDistribution::getId,
                            LevelDistribution::getCount
                    ));
        } catch (Exception e) {
            logger.error("Error fetching level distribution", e);
            throw e;
        }
    }
}
