package com.example.offerbrowserprototype.infrastructure.service;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.external.JobOfferProviderFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalJobOfferService {

    private final JobOfferProviderFactory jobOfferProviderFactory;

    public ExternalJobOfferService(JobOfferProviderFactory jobOfferProviderFactory) {
        this.jobOfferProviderFactory = jobOfferProviderFactory;
    }

    public List<OfferDTO> fetchExternalOffers() {
        return jobOfferProviderFactory.fetchAllOffers();
    }

    public List<String> getProviderNames() {
        return jobOfferProviderFactory.getAvailableProviders();
    }
}
