package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.external.JobOfferProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class OfferPushHandlerTest {

    private List<JobOfferProvider> jobOfferProviders;
    private OfferRetrievalHandler retrievalHandler;
    private OfferPushHandler offerPushHandler;

    @BeforeEach
    void setUp() {
        jobOfferProviders = mock(List.class);
        retrievalHandler = mock(OfferRetrievalHandler.class);
        offerPushHandler = new OfferPushHandler(jobOfferProviders, retrievalHandler);
    }

    @Test
    void shouldPushOfferToProviderSuccessfully() {
        // Mockowanie danych
        String offerId = "123";
        String providerName = "TestProvider";

        OfferDTO mockOffer = new OfferDTO();
        mockOffer.setId(offerId);
        mockOffer.setTitle("Test Offer");

        JobOfferProvider mockProvider = mock(JobOfferProvider.class);
        when(mockProvider.getProviderName()).thenReturn(providerName);

        // Mockowanie zachowania stream() i filtracji
        when(jobOfferProviders.stream())
                .thenReturn(Stream.of(mockProvider));

        when(retrievalHandler.getOffer(offerId)).thenReturn(mockOffer);

        // Wywołanie metody
        offerPushHandler.pushOfferToProvider(offerId, providerName);

        // Weryfikacja
        verify(mockProvider, times(1)).pushOffer(mockOffer);
    }

    @Test
    void shouldThrowExceptionIfOfferNotFound() {
        // Mockowanie danych
        String offerId = "123";
        String providerName = "TestProvider";

        when(retrievalHandler.getOffer(offerId)).thenReturn(null);

        // Wywołanie i weryfikacja
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> offerPushHandler.pushOfferToProvider(offerId, providerName))
                .withMessage("Offer not found");
    }

    @Test
    void shouldThrowExceptionIfProviderNotFound() {
        // Mockowanie danych
        String offerId = "123";
        String providerName = "NonExistentProvider";

        OfferDTO mockOffer = new OfferDTO();
        mockOffer.setId(offerId);
        mockOffer.setTitle("Test Offer");

        when(retrievalHandler.getOffer(offerId)).thenReturn(mockOffer);

        // Mockowanie pustego strumienia
        when(jobOfferProviders.stream())
                .thenReturn(Stream.empty());

        // Wywołanie i weryfikacja
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> offerPushHandler.pushOfferToProvider(offerId, providerName))
                .withMessage("Provider not found");
    }
}
