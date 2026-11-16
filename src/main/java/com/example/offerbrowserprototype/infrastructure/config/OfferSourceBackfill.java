package com.example.offerbrowserprototype.infrastructure.config;

import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.infrastructure.repository.NiceToHaveRepository;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.RequirementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Idempotent startup migration: offers imported before the {@code source}
 * column was filled get it from their URL domain, so the per-portal views
 * (which filter by source) keep showing historical No Fluff Jobs data.
 */
@Component
@Order(0)
public class OfferSourceBackfill implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(OfferSourceBackfill.class);

    private final OfferRepository offerRepository;
    private final RequirementRepository requirementRepository;
    private final NiceToHaveRepository niceToHaveRepository;

    public OfferSourceBackfill(OfferRepository offerRepository,
                               RequirementRepository requirementRepository,
                               NiceToHaveRepository niceToHaveRepository) {
        this.offerRepository = offerRepository;
        this.requirementRepository = requirementRepository;
        this.niceToHaveRepository = niceToHaveRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        int offers = offerRepository.backfillSource(OfferSource.NOFLUFF.value(), "https://nofluffjobs.com/%")
                + offerRepository.backfillSource(OfferSource.NOFLUFF.value(), "https://www.nofluffjobs.com/%");
        offers += offerRepository.backfillSource(OfferSource.THEPROTOCOL.value(), "https://theprotocol.it/%");
        int requirements = requirementRepository.backfillSource(OfferSource.NOFLUFF.value());
        int niceToHave = niceToHaveRepository.backfillSource(OfferSource.NOFLUFF.value());
        if (offers + requirements + niceToHave > 0) {
            logger.info("Source backfill: {} offers, {} requirement facts, {} nice-to-have facts",
                    offers, requirements, niceToHave);
        }
    }
}
