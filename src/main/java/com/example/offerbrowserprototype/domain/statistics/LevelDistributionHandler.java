package com.example.offerbrowserprototype.domain.statistics;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
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
        return getLevelDistribution(null);
    }

    public Map<String, Long> getLevelDistribution(OfferSource source) {
        logger.info("Fetching level distribution (source={})...", source);

        var rawDistribution = source == null
                ? offerRepository.getLevelDistributionSimple()
                : offerRepository.getLevelDistributionBySource(source.value());

        return rawDistribution.stream()
                .collect(Collectors.toMap(
                        LevelDistributionProjection::getId,
                        LevelDistributionProjection::getCount
                ));
    }
}

