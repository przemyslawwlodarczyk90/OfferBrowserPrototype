package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.external.JobOfferProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfferPushHandler {

    private final List<JobOfferProvider> jobOfferProviders;
    private final OfferDetailsHandler detailsHandler;

    public OfferPushHandler(
            List<JobOfferProvider> jobOfferProviders,
            OfferDetailsHandler detailsHandler
    ) {
        this.jobOfferProviders = jobOfferProviders;
        this.detailsHandler = detailsHandler;
    }

    public void pushOfferToProvider(Long offerId, String providerName) {

        OfferDTO offer = detailsHandler.getOfferById(offerId);

        JobOfferProvider provider = jobOfferProviders.stream()
                .filter(p -> p.getProviderName().equalsIgnoreCase(providerName))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Provider not found: " + providerName));

        provider.pushOffer(offer);
    }
}
