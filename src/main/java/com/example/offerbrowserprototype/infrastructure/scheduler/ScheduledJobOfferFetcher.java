package com.example.offerbrowserprototype.infrastructure.scheduler;

import com.example.offerbrowserprototype.infrastructure.service.ExternalJobOfferService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.logging.Logger;

@Component
@ConditionalOnProperty(name = "job.offer.scheduler.enabled", havingValue = "true", matchIfMissing = true) // Adnotacja warunkowa
public class ScheduledJobOfferFetcher {

    private static final Logger LOGGER = Logger.getLogger(ScheduledJobOfferFetcher.class.getName());
    private final ExternalJobOfferService externalJobOfferService;

    public ScheduledJobOfferFetcher(ExternalJobOfferService externalJobOfferService) {
        this.externalJobOfferService = externalJobOfferService;
    }

    // Zadanie wykonywane zgodnie z harmonogramem zdefiniowanym w application.properties
    @Scheduled(cron = "${job.offer.fetch.cron}")
    public void fetchOffersFromProviders() {
        LOGGER.info("Scheduled fetching of job offers started.");

        try {
            externalJobOfferService.fetchExternalOffers();
            LOGGER.info("Successfully fetched external job offers.");
        } catch (Exception e) {
            LOGGER.severe("Error occurred while fetching offers: " + e.getMessage());
        }
    }
}
