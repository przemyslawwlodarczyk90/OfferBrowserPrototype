package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Klasa testowa dla `OfferNotAppliedHandler`.
 * Sprawdza poprawność pobierania ofert, do których nie złożono aplikacji.
 */
class OfferNotAppliedHandlerTest {

    private OfferRepository offerRepository;
    private OfferMapper offerMapper;
    private OfferNotAppliedHandler offerNotAppliedHandler;

    /**
     * Inicjalizacja testów.
     * Tworzenie mocków dla `OfferRepository` i `OfferMapper` oraz instancji `OfferNotAppliedHandler`.
     */
    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        offerMapper = mock(OfferMapper.class);
        offerNotAppliedHandler = new OfferNotAppliedHandler(offerRepository, offerMapper);
    }

    /**
     * Test sprawdza, czy metoda `getNotAppliedOffers` zwraca poprawne dane dla ofert, do których nie złożono aplikacji.
     */
    @Test
    void shouldReturnNotAppliedOffers() {
        // Given - Przygotowanie danych testowych
        Offer offer1 = new Offer();
        offer1.setId("1");
        offer1.setTitle("Java Developer");
        offer1.setFetchedAt(LocalDateTime.now());

        Offer offer2 = new Offer();
        offer2.setId("2");
        offer2.setTitle("React Developer");
        offer2.setFetchedAt(LocalDateTime.now().minusDays(1));

        List<Offer> offers = Arrays.asList(offer1, offer2);

        OfferDTO dto1 = new OfferDTO();
        dto1.setId("1");
        dto1.setTitle("Java Developer");

        OfferDTO dto2 = new OfferDTO();
        dto2.setId("2");
        dto2.setTitle("React Developer");

        when(offerRepository.findByAppliedFalseOrderByFetchedAtDesc()).thenReturn(offers);
        when(offerMapper.toDTO(offer1)).thenReturn(dto1);
        when(offerMapper.toDTO(offer2)).thenReturn(dto2);

        // When - Wywołanie metody
        List<OfferDTO> result = offerNotAppliedHandler.getNotAppliedOffers();

        // Then - Weryfikacja wyników
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Java Developer", result.get(0).getTitle());
        assertEquals("2", result.get(1).getId());
        assertEquals("React Developer", result.get(1).getTitle());

        verify(offerRepository, times(1)).findByAppliedFalseOrderByFetchedAtDesc();
        verify(offerMapper, times(1)).toDTO(offer1);
        verify(offerMapper, times(1)).toDTO(offer2);
    }

    /**
     * Test sprawdza, czy metoda `getNotAppliedOffers` zwraca pustą listę, gdy brak ofert.
     */
    @Test
    void shouldReturnEmptyListWhenNoNotAppliedOffersExist() {
        // Given - Przygotowanie danych testowych
        when(offerRepository.findByAppliedFalseOrderByFetchedAtDesc()).thenReturn(List.of());

        // When - Wywołanie metody
        List<OfferDTO> result = offerNotAppliedHandler.getNotAppliedOffers();

        // Then - Weryfikacja wyników
        assertEquals(0, result.size());
        verify(offerRepository, times(1)).findByAppliedFalseOrderByFetchedAtDesc();
        verifyNoInteractions(offerMapper); // Mapper nie powinien być wywołany
    }
}
