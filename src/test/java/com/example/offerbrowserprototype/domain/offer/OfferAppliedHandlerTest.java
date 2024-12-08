package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OfferAppliedHandlerTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferMapper offerMapper;

    private OfferAppliedHandler offerAppliedHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        offerAppliedHandler = new OfferAppliedHandler(offerRepository, offerMapper);
    }

    @Test
    void shouldReturnAppliedOffersInDescendingOrderByFetchedAt() {
        // Given - Dane testowe
        Offer offer1 = new Offer();
        offer1.setId("1");
        offer1.setTitle("Java Developer");
        offer1.setFetchedAt(LocalDateTime.now().minusDays(1));
        offer1.setApplied(true);

        Offer offer2 = new Offer();
        offer2.setId("2");
        offer2.setTitle("React Developer");
        offer2.setFetchedAt(LocalDateTime.now());
        offer2.setApplied(true);

        List<Offer> appliedOffers = Arrays.asList(offer2, offer1);

        OfferDTO offerDTO1 = new OfferDTO("1", "Java Developer", "Description 1", "Location 1", "5000-7000 PLN", "Java, Spring", true, offer1.getFetchedAt());
        OfferDTO offerDTO2 = new OfferDTO("2", "React Developer", "Description 2", "Location 2", "6000-8000 PLN", "React, Node.js", true, offer2.getFetchedAt());

        Mockito.when(offerRepository.findByAppliedTrueOrderByFetchedAtDesc()).thenReturn(appliedOffers);
        Mockito.when(offerMapper.toDTO(offer1)).thenReturn(offerDTO1);
        Mockito.when(offerMapper.toDTO(offer2)).thenReturn(offerDTO2);

        // When - Wywołanie metody
        List<OfferDTO> result = offerAppliedHandler.getAppliedOffers();

        // Then - Sprawdzenie wyników
        assertEquals(2, result.size(), "Powinna być zwrócona lista dwóch zaaplikowanych ofert.");
        assertEquals("2", result.get(0).getId(), "Pierwsza oferta w liście powinna mieć ID 2.");
        assertEquals("1", result.get(1).getId(), "Druga oferta w liście powinna mieć ID 1.");

        verify(offerRepository, times(1)).findByAppliedTrueOrderByFetchedAtDesc();
        verify(offerMapper, times(1)).toDTO(offer1);
        verify(offerMapper, times(1)).toDTO(offer2);
    }

    @Test
    void shouldReturnEmptyListWhenNoAppliedOffers() {
        // Given - Dane testowe
        Mockito.when(offerRepository.findByAppliedTrueOrderByFetchedAtDesc()).thenReturn(List.of());

        // When - Wywołanie metody
        List<OfferDTO> result = offerAppliedHandler.getAppliedOffers();

        // Then - Sprawdzenie wyników
        assertEquals(0, result.size(), "Powinna być zwrócona pusta lista, gdy brak zaaplikowanych ofert.");

        verify(offerRepository, times(1)).findByAppliedTrueOrderByFetchedAtDesc();
        verifyNoInteractions(offerMapper);
    }
}
