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

import static org.junit.jupiter.api.Assertions.*;
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
    void shouldReturnEmptyListWhenNoAppliedOffers() {
        // Given
        when(offerRepository.findByAppliedTrueOrderByFetchedAtDesc()).thenReturn(List.of());

        // When
        List<OfferDTO> result = offerAppliedHandler.getAppliedOffers();

        // Then
        assertNotNull(result, "The result list should not be null.");
        assertTrue(result.isEmpty(), "The list should be empty when no applied offers are present.");

        verify(offerRepository, times(1)).findByAppliedTrueOrderByFetchedAtDesc();
        verifyNoInteractions(offerMapper);
    }
}