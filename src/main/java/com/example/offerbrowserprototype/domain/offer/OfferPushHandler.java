package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.external.JobOfferProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OfferPushHandler {

    private final List<JobOfferProvider> jobOfferProviders;
    private final OfferRetrievalHandler retrievalHandler;

    public OfferPushHandler(List<JobOfferProvider> jobOfferProviders, OfferRetrievalHandler retrievalHandler) {
        this.jobOfferProviders = jobOfferProviders;
        this.retrievalHandler = retrievalHandler;
    }

    public void pushOfferToProvider(String offerId, String providerName) {
        // Znajdź ofertę na podstawie ID
        OfferDTO offer = retrievalHandler.getOffer(offerId);
        if (offer == null) {
            throw new IllegalArgumentException("Offer not found");
        }

        // Znajdź providera na podstawie nazwy
        Optional<JobOfferProvider> providerOpt = jobOfferProviders.stream()
                .filter(provider -> provider.getProviderName().equalsIgnoreCase(providerName))
                .findFirst();

        if (providerOpt.isEmpty()) {
            throw new IllegalArgumentException("Provider not found");
        }

        JobOfferProvider provider = providerOpt.get();

        // Wypchnij ofertę do providera
        provider.pushOffer(offer);
    }
}
