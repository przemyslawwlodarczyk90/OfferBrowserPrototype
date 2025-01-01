package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.external.JobOfferProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Klasa testowa dla `OfferPushHandler`.
 * Sprawdza poprawność wypychania ofert do providerów.
 */
class OfferPushHandlerTest {

    private OfferPushHandler offerPushHandler;
    private OfferRetrievalHandler retrievalHandler;
    private JobOfferProvider mockProvider1;
    private JobOfferProvider mockProvider2;

    /**
     * Przygotowanie środowiska testowego.
     */
    @BeforeEach
    void setUp() {
        retrievalHandler = mock(OfferRetrievalHandler.class);
        mockProvider1 = mock(JobOfferProvider.class);
        mockProvider2 = mock(JobOfferProvider.class);

        when(mockProvider1.getProviderName()).thenReturn("Provider1");
        when(mockProvider2.getProviderName()).thenReturn("Provider2");

        offerPushHandler = new OfferPushHandler(List.of(mockProvider1, mockProvider2), retrievalHandler);
    }

    /**
     * Test sprawdza poprawne wypchnięcie oferty do wybranego providera.
     */
    @Test
    void shouldPushOfferToProviderSuccessfully() {
        // Given - Dane testowe
        OfferDTO offer = new OfferDTO("1", "Java Developer", "Great job", "Warsaw", "10,000-15,000 PLN", "Amazon", "Java", false, "Company A", false, null);
        when(retrievalHandler.getOffer("1")).thenReturn(offer);

        // When - Wywołanie metody
        offerPushHandler.pushOfferToProvider("1", "Provider1");

        // Then - Weryfikacja interakcji
        verify(retrievalHandler, times(1)).getOffer("1");
        verify(mockProvider1, times(1)).pushOffer(offer);
        verifyNoInteractions(mockProvider2);
    }

    /**
     * Test sprawdza, czy metoda rzuca wyjątek, gdy oferta nie zostanie znaleziona.
     */
    @Test
    void shouldThrowExceptionWhenOfferNotFound() {
        // Given - Brak oferty w retrieverze
        when(retrievalHandler.getOffer("2")).thenReturn(null);

        // When/Then - Wywołanie i weryfikacja wyjątku
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> offerPushHandler.pushOfferToProvider("2", "Provider1"));

        assertEquals("Offer not found", exception.getMessage());
        verify(retrievalHandler, times(1)).getOffer("2");
        verifyNoInteractions(mockProvider1, mockProvider2);
    }

    /**
     * Test sprawdza, czy metoda rzuca wyjątek, gdy provider nie zostanie znaleziony.
     */
    @Test
    void shouldThrowExceptionWhenProviderNotFound() {
        // Given - Istniejąca oferta
        OfferDTO offer = new OfferDTO("3", "Frontend Developer", "Exciting opportunity", "Krakow", "8,000-12,000 PLN", "React", false, "Company B", false, null);
        when(retrievalHandler.getOffer("3")).thenReturn(offer);

        // When/Then - Wywołanie i weryfikacja wyjątku
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> offerPushHandler.pushOfferToProvider("3", "UnknownProvider"));

        assertEquals("Provider not found", exception.getMessage());

        // Verify retrieval handler was called
        verify(retrievalHandler, times(1)).getOffer("3");

        // Sprawdzenie, czy interakcje z providerami były, ale brak operacji push
        verify(mockProvider1, times(1)).getProviderName();
        verify(mockProvider2, times(1)).getProviderName();
        verifyNoMoreInteractions(mockProvider1, mockProvider2);
    }

    /**
     * Test sprawdza, czy metoda działa poprawnie w przypadku ignorowania wielkości liter w nazwie providera.
     */
    @Test
    void shouldHandleCaseInsensitiveProviderName() {
        // Given - Dane testowe
        OfferDTO offer = new OfferDTO("4", "Backend Developer", "Amazing role", "Gdansk", "12,000-18,000 PLN", "Spring Boot", false, "Company C", false, null);
        when(retrievalHandler.getOffer("4")).thenReturn(offer);

        // When - Wywołanie metody z nazwą providera w innej wielkości liter
        offerPushHandler.pushOfferToProvider("4", "provider1");

        // Then - Weryfikacja interakcji
        verify(retrievalHandler, times(1)).getOffer("4");
        verify(mockProvider1, times(1)).pushOffer(offer);
        verifyNoInteractions(mockProvider2);
    }
}
