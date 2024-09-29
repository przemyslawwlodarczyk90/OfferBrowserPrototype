package com.example.offerbrowserprototype.external;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
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
            // Próba pobrania danych z zewnętrznego API
            OfferDTO[] offers = restTemplate.getForObject(apiUrl, OfferDTO[].class);
            return Arrays.asList(offers);
        } catch (HttpClientErrorException e) {
            // Obsługa błędów klienta HTTP (np. 4xx)
            LOGGER.severe("Client error while fetching offers from " + getProviderName() + ": " + e.getMessage());
        } catch (HttpServerErrorException e) {
            // Obsługa błędów serwera HTTP (np. 5xx)
            LOGGER.severe("Server error while fetching offers from " + getProviderName() + ": " + e.getMessage());
        } catch (ResourceAccessException e) {
            // Obsługa błędów dostępu do zasobów (np. problemy z siecią)
            LOGGER.severe("Resource access error while fetching offers from " + getProviderName() + ": " + e.getMessage());
        } catch (RestClientException e) {
            // Obsługa ogólnych błędów RestTemplate
            LOGGER.severe("Rest client error while fetching offers from " + getProviderName() + ": " + e.getMessage());
        } catch (Exception e) {
            // Obsługa innych nieoczekiwanych wyjątków
            LOGGER.severe("Unexpected error while fetching offers from " + getProviderName() + ": " + e.getMessage());
        }

        // W razie błędu zwracamy pustą listę, aby obsłużyć to na poziomie wyższym
        return List.of();
    }

    @Override
    public String getProviderName() {
        return "ProviderOne";
    }
}
