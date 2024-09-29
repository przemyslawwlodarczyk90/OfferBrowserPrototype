package com.example.offerbrowserprototype.external;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class ProviderTwoJobOfferProvider implements JobOfferProvider {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public ProviderTwoJobOfferProvider(RestTemplate restTemplate,
                                       @Value("${provider.two.api.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    @Override
    public List<OfferDTO> fetchOffers() {
        OfferDTO[] offers = restTemplate.getForObject(apiUrl, OfferDTO[].class);
        return Arrays.asList(offers);
    }

    @Override
    public String getProviderName() {
        return "ProviderTwo";
    }
}
