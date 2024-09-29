package com.example.offerbrowserprototype.external;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobOfferProviderFactory {

    private final List<JobOfferProvider> jobOfferProviders;

    public JobOfferProviderFactory(List<JobOfferProvider> jobOfferProviders) {
        this.jobOfferProviders = jobOfferProviders;
    }

    public List<OfferDTO> fetchAllOffers() {
        // Pobierz oferty ze wszystkich zarejestrowanych providerów
        return jobOfferProviders.stream()
                .flatMap(provider -> provider.fetchOffers().stream())
                .collect(Collectors.toList());
    }

    public List<String> getAvailableProviders() {
        return jobOfferProviders.stream()
                .map(JobOfferProvider::getProviderName)
                .collect(Collectors.toList());
    }
}
