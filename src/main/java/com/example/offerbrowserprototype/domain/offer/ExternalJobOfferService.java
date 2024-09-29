package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import com.example.offerbrowserprototype.external.JobOfferProviderFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalJobOfferService {

    private final JobOfferProviderFactory jobOfferProviderFactory;

    public ExternalJobOfferService(JobOfferProviderFactory jobOfferProviderFactory) {
        this.jobOfferProviderFactory = jobOfferProviderFactory;
    }

    @Async
    public List<OfferDTO> fetchExternalOffers() {
        return jobOfferProviderFactory.fetchAllOffers();
    }

    public List<String> getProviderNames() {
        return jobOfferProviderFactory.getAvailableProviders();
    }
}
