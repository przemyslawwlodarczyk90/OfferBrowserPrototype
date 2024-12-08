package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

/**
 * Klasa testowa dla `OfferDeletionHandler`.
 * Sprawdza poprawność usuwania ofert z repozytorium.
 */
class OfferDeletionHandlerTest {

    private OfferRepository offerRepository;
    private OfferDeletionHandler offerDeletionHandler;

    /**
     * Inicjalizacja testów.
     * Tworzenie mocka dla `OfferRepository` oraz instancji `OfferDeletionHandler`.
     */
    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        offerDeletionHandler = new OfferDeletionHandler(offerRepository);
    }

    /**
     * Test sprawdza, czy metoda `deleteOffer` poprawnie usuwa ofertę o podanym ID.
     */
    @Test
    void shouldDeleteOfferById() {
        // Given - Przygotowanie danych testowych
        String offerId = "1";

        // When - Wywołanie metody
        offerDeletionHandler.deleteOffer(offerId);

        // Then - Weryfikacja wyników
        verify(offerRepository, times(1)).deleteById(offerId);
    }

    /**
     * Test sprawdza, czy metoda `deleteOffer` nie rzuca wyjątku,
     * gdy próbujemy usunąć ofertę o nieistniejącym ID.
     */
    @Test
    void shouldNotThrowExceptionWhenOfferIdDoesNotExist() {
        // Given - Przygotowanie danych testowych
        String offerId = "nonexistent-id";

        // When - Wywołanie metody
        offerDeletionHandler.deleteOffer(offerId);

        // Then - Weryfikacja wyników
        verify(offerRepository, times(1)).deleteById(offerId);
    }
}
