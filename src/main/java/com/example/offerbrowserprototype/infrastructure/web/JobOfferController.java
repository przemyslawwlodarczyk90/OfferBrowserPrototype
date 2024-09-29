package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.infrastructure.service.ExternalJobOfferService;
import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
@Tag(name = "Job Offers", description = "Scheduled operations related to job offers fetching")
public class JobOfferController {

    private static final Logger LOGGER = Logger.getLogger(JobOfferController.class.getName());
    private final ExternalJobOfferService externalJobOfferService;

    // Odczyt cron expression z application.properties
    @Value("${job.offer.fetch.cron}")
    private String cronExpression;

    public JobOfferController(ExternalJobOfferService externalJobOfferService) {
        this.externalJobOfferService = externalJobOfferService;
    }

    @Operation(summary = "Fetch job offers automatically", description = "Automatically fetches job offers from external providers based on a cron schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scheduled fetching executed successfully"),
            @ApiResponse(responseCode = "500", description = "Error occurred while fetching job offers")
    })
    // Metoda harmonogramowana – pobiera oferty zgodnie z harmonogramem ustawionym w application.properties
    @Scheduled(cron = "${job.offer.fetch.cron}")
    public void fetchOffersAutomatically() {
        LOGGER.info("Scheduled fetching of job offers started.");

        try {
            List<OfferDTO> offers = externalJobOfferService.fetchExternalOffers();
            // Dodatkowa logika przetwarzania ofert
            LOGGER.info("Successfully fetched " + offers.size() + " offers automatically.");
        } catch (Exception e) {
            LOGGER.severe("Error occurred during scheduled fetching of offers: " + e.getMessage());
        }
    }
}
