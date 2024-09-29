package com.example.offerbrowserprototype.infrastructure.scheduler;


import com.example.offerbrowserprototype.infrastructure.service.ExternalJobOfferService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class ScheduledJobOfferFetcher {

    private static final Logger LOGGER = Logger.getLogger(ScheduledJobOfferFetcher.class.getName());
    private final ExternalJobOfferService externalJobOfferService;

    public ScheduledJobOfferFetcher(ExternalJobOfferService externalJobOfferService) {
        this.externalJobOfferService = externalJobOfferService;
    }

    // Zadanie wykonywane co godzinę (Cron expression: "0 0 * * * *" oznacza: na początku każdej godziny)
    @Scheduled(cron = "0 0 * * * *")
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
