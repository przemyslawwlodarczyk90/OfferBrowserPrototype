package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ExternalJobOfferService {

    private final RestTemplate restTemplate;
    private final String EXTERNAL_API_URL = "https://api.example.com/offers"; // Przykładowy URL

    public ExternalJobOfferService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<OfferDTO> fetchExternalOffers() {
        OfferDTO[] offers = restTemplate.getForObject(EXTERNAL_API_URL, OfferDTO[].class);
        return Arrays.asList(offers);
    }
}
