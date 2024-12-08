package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferRetrievalHandlerTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferMapper offerMapper;

    private OfferRetrievalHandler offerRetrievalHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        offerRetrievalHandler = new OfferRetrievalHandler(offerRepository, offerMapper);
    }

    /**
     * Test sprawdzający, czy zwracana jest poprawna oferta na podstawie ID.
     */
    @Test
    void shouldReturnOfferById() {
        // Given - dane testowe
        String offerId = "1";
        Offer offer = new Offer();
        offer.setId(offerId);
        offer.setTitle("Java Developer");
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offerId);
        offerDTO.setTitle("Java Developer");

        when(offerRepository.findById(offerId)).thenReturn(Optional.of(offer));
        when(offerMapper.toDTO(offer)).thenReturn(offerDTO);

        // When - wywołanie metody
        OfferDTO result = offerRetrievalHandler.getOffer(offerId);

        // Then - sprawdzenie wyników
        assertNotNull(result);
        assertEquals(offerId, result.getId());
        assertEquals("Java Developer", result.getTitle());
        verify(offerRepository, times(1)).findById(offerId);
        verify(offerMapper, times(1)).toDTO(offer);
    }

    /**
     * Test sprawdzający, czy zwracana jest null, jeśli oferta nie istnieje.
     */
    @Test
    void shouldReturnNullWhenOfferNotFoundById() {
        // Given - dane testowe
        String offerId = "2";
        when(offerRepository.findById(offerId)).thenReturn(Optional.empty());

        // When - wywołanie metody
        OfferDTO result = offerRetrievalHandler.getOffer(offerId);

        // Then - sprawdzenie wyników
        assertNull(result);
        verify(offerRepository, times(1)).findById(offerId);
        verifyNoInteractions(offerMapper);
    }

    /**
     * Test sprawdzający, czy zwracane są wszystkie oferty.
     */
    @Test
    void shouldReturnAllOffers() {
        // Given - dane testowe
        Offer offer1 = new Offer();
        offer1.setId("1");
        offer1.setTitle("Java Developer");
        offer1.setFetchedAt(LocalDateTime.now());

        Offer offer2 = new Offer();
        offer2.setId("2");
        offer2.setTitle("Frontend Developer");
        offer2.setFetchedAt(LocalDateTime.now().minusDays(1));

        OfferDTO offerDTO1 = new OfferDTO();
        offerDTO1.setId("1");
        offerDTO1.setTitle("Java Developer");

        OfferDTO offerDTO2 = new OfferDTO();
        offerDTO2.setId("2");
        offerDTO2.setTitle("Frontend Developer");

        when(offerRepository.findAllByOrderByFetchedAtDesc()).thenReturn(List.of(offer1, offer2));
        when(offerMapper.toDTO(offer1)).thenReturn(offerDTO1);
        when(offerMapper.toDTO(offer2)).thenReturn(offerDTO2);

        // When - wywołanie metody
        List<OfferDTO> result = offerRetrievalHandler.getAllOffers();

        // Then - sprawdzenie wyników
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Java Developer", result.get(0).getTitle());
        assertEquals("2", result.get(1).getId());
        assertEquals("Frontend Developer", result.get(1).getTitle());
        verify(offerRepository, times(1)).findAllByOrderByFetchedAtDesc();
        verify(offerMapper, times(1)).toDTO(offer1);
        verify(offerMapper, times(1)).toDTO(offer2);
    }
}
