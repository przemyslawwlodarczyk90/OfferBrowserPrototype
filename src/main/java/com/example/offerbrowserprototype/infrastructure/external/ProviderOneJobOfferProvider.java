package com.example.offerbrowserprototype.infrastructure.external;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ProviderOneJobOfferProvider implements JobOfferProvider {

    private static final Logger LOGGER = Logger.getLogger(ProviderOneJobOfferProvider.class.getName());
    private final RestTemplate restTemplate;
    private final String apiUrl;

    public ProviderOneJobOfferProvider(RestTemplate restTemplate,
                                       @Value("${provider.one.api.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    @Override
    public List<OfferDTO> fetchOffers() {
        try {

            OfferDTO[] offers = restTemplate.getForObject(apiUrl, OfferDTO[].class);
            return Arrays.asList(offers);
        } catch (HttpClientErrorException e) {
            LOGGER.severe("Client error while fetching offers from " + getProviderName() + ": " + e.getMessage());
        } catch (HttpServerErrorException e) {
            LOGGER.severe("Server error while fetching offers from " + getProviderName() + ": " + e.getMessage());
        } catch (ResourceAccessException e) {
            LOGGER.severe("Resource access error while fetching offers from " + getProviderName() + ": " + e.getMessage());
        } catch (RestClientException e) {
            LOGGER.severe("Rest client error while fetching offers from " + getProviderName() + ": " + e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Unexpected error while fetching offers from " + getProviderName() + ": " + e.getMessage());
        }

        return List.of();
    }

    @Override
    public String getProviderName() {
        return "ProviderOne";
    }

    @Override
    public void pushOffer(OfferDTO offer) {
        try {
            restTemplate.postForEntity(apiUrl + "/push", offer, Void.class);
            LOGGER.info("Offer pushed successfully to ProviderOne");
        } catch (Exception e) {
            LOGGER.severe("Error pushing offer to ProviderOne: " + e.getMessage());
            throw new RuntimeException("Failed to push offer to provider", e);
        }
    }
}
