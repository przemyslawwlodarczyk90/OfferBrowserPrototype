package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Klasa testowa dla `OfferDetailsHandler`.
 * Sprawdza poprawność pobierania szczegółów oferty na podstawie ID.
 */
class OfferDetailsHandlerTest {

    private OfferRepository offerRepository;
    private OfferMapper offerMapper;
    private OfferDetailsHandler offerDetailsHandler;

    /**
     * Inicjalizacja testów.
     * Tworzenie mocków dla `OfferRepository` i `OfferMapper` oraz instancji `OfferDetailsHandler`.
     */
    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        offerMapper = mock(OfferMapper.class);
        offerDetailsHandler = new OfferDetailsHandler(offerRepository, offerMapper);
    }

    /**
     * Test sprawdza, czy metoda `getOfferById` zwraca poprawne dane dla istniejącej oferty.
     */
    @Test
    void shouldReturnOfferDetailsWhenOfferExists() {
        // Given - Przygotowanie danych testowych
        String offerId = "1";
        Offer mockOffer = new Offer();
        mockOffer.setId(offerId);
        mockOffer.setTitle("Java Developer");

        OfferDTO mockOfferDTO = new OfferDTO();
        mockOfferDTO.setId(offerId);
        mockOfferDTO.setTitle("Java Developer");

        when(offerRepository.findById(offerId)).thenReturn(Optional.of(mockOffer));
        when(offerMapper.toDTO(mockOffer)).thenReturn(mockOfferDTO);

        // When - Wywołanie metody
        OfferDTO result = offerDetailsHandler.getOfferById(offerId);

        // Then - Weryfikacja wyników
        assertNotNull(result);
        assertEquals(offerId, result.getId());
        assertEquals("Java Developer", result.getTitle());
        verify(offerRepository, times(1)).findById(offerId);
        verify(offerMapper, times(1)).toDTO(mockOffer);
    }

    /**
     * Test sprawdza, czy metoda `getOfferById` rzuca wyjątek dla nieistniejącej oferty.
     */
    @Test
    void shouldThrowExceptionWhenOfferDoesNotExist() {
        // Given - Przygotowanie danych testowych
        String offerId = "nonexistent-id";

        when(offerRepository.findById(offerId)).thenReturn(Optional.empty());

        // When & Then - Wywołanie metody i sprawdzenie, czy rzucono wyjątek
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            offerDetailsHandler.getOfferById(offerId);
        });

        assertEquals("Offer not found", exception.getMessage());
        verify(offerRepository, times(1)).findById(offerId);
        verifyNoInteractions(offerMapper); // Mapper nie powinien być wywołany
    }
}
