package com.example.offerbrowserprototype.external;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class JobOfferProviderFactory {

    private static final Logger LOGGER = Logger.getLogger(JobOfferProviderFactory.class.getName());
    private final List<JobOfferProvider> jobOfferProviders;

    public JobOfferProviderFactory(List<JobOfferProvider> jobOfferProviders) {
        this.jobOfferProviders = jobOfferProviders;
    }

    public List<OfferDTO> fetchAllOffers() {
        List<OfferDTO> allOffers = new ArrayList<>();

        for (JobOfferProvider provider : jobOfferProviders) {
            try {
                List<OfferDTO> offers = provider.fetchOffers();
                allOffers.addAll(offers);
                LOGGER.info("Fetched " + offers.size() + " offers from provider: " + provider.getProviderName());
            } catch (Exception e) {
                LOGGER.severe("Failed to fetch offers from provider: " + provider.getProviderName() + ", error: " + e.getMessage());
            }
        }

        return allOffers;
    }

    // Dodanie metody getAvailableProviders
    public List<String> getAvailableProviders() {
        List<String> providerNames = new ArrayList<>();
        for (JobOfferProvider provider : jobOfferProviders) {
            providerNames.add(provider.getProviderName());
        }
        return providerNames;
    }
}
