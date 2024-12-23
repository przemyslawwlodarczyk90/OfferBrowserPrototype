package com.example.offerbrowserprototype.infrastructure.external;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Service
public class JobOfferProviderFactory {

    private static final Logger LOGGER = Logger.getLogger(JobOfferProviderFactory.class.getName());
    private final List<JobOfferProvider> jobOfferProviders;

    public JobOfferProviderFactory(List<JobOfferProvider> jobOfferProviders) {
        this.jobOfferProviders = jobOfferProviders;
    }

    public List<OfferDTO> fetchAllOffers() {
        List<CompletableFuture<List<OfferDTO>>> futureOffers = new ArrayList<>();


        for (JobOfferProvider provider : jobOfferProviders) {
            futureOffers.add(fetchOffersAsync(provider));
        }


        List<OfferDTO> allOffers = new ArrayList<>();
        for (CompletableFuture<List<OfferDTO>> future : futureOffers) {
            try {
                allOffers.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.severe("Error while retrieving offers: " + e.getMessage());
            }
        }

        return allOffers;
    }


    @Async
    @Retryable(value = { Exception.class }, maxAttempts = 3)
    public CompletableFuture<List<OfferDTO>> fetchOffersAsync(JobOfferProvider provider) {
        List<OfferDTO> offers = new ArrayList<>();

        try {
            offers = provider.fetchOffers();
            validateOffers(offers);
            LOGGER.info("Fetched " + offers.size() + " offers from provider: " + provider.getProviderName());
        } catch (Exception e) {
            LOGGER.severe("Error fetching offers from provider: " + provider.getProviderName() + ". Details: " + e.getMessage());
        }

        return CompletableFuture.completedFuture(offers);
    }


    private void validateOffers(List<OfferDTO> offers) {

        if (offers == null || offers.isEmpty()) {
            throw new IllegalArgumentException("Received invalid or empty offers list.");
        }
        for (OfferDTO offer : offers) {
            if (offer.getId() == null || offer.getTitle() == null) {
                throw new IllegalArgumentException("Invalid offer detected: " + offer);
            }
        }
    }

    public List<String> getAvailableProviders() {
        List<String> providerNames = new ArrayList<>();
        for (JobOfferProvider provider : jobOfferProviders) {
            providerNames.add(provider.getProviderName());
        }
        return providerNames;
    }
}
