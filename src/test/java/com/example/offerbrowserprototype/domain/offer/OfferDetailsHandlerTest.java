package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OfferDetailsHandlerTest {

    private OfferRepository offerRepository;
    private OfferMapper offerMapper;
    private OfferDetailsHandler offerDetailsHandler;

    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        offerMapper = mock(OfferMapper.class);
        offerDetailsHandler = new OfferDetailsHandler(offerRepository, offerMapper);
    }

    @Test
    void shouldReturnOfferDTOWhenOfferExists() {
        // Given
        String offerId = "123";
        Offer offer = new Offer();
        offer.setId(offerId);
        offer.setTitle("Java Developer");
        offer.setDescription("Job description");
        offer.setLocation("Warsaw");
        offer.setOfferUrl("https://example.com/offer/123");

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offerId);
        offerDTO.setTitle("Java Developer");
        offerDTO.setDescription("Job description");
        offerDTO.setLocation("Warsaw");
        offerDTO.setOfferUrl("https://example.com/offer/123");

        when(offerRepository.findById(offerId)).thenReturn(Optional.of(offer));
        when(offerMapper.toDTO(offer)).thenReturn(offerDTO);

        // When
        OfferDTO result = offerDetailsHandler.getOfferById(offerId);

        // Then
        assertThat(result).isEqualTo(offerDTO);
        verify(offerRepository, times(1)).findById(offerId);
        verify(offerMapper, times(1)).toDTO(offer);
    }

    @Test
    void shouldThrowExceptionWhenOfferDoesNotExist() {
        // Given
        String nonExistentOfferId = "non-existent-id";
        when(offerRepository.findById(nonExistentOfferId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            offerDetailsHandler.getOfferById(nonExistentOfferId);
        });

        assertThat(exception.getMessage()).isEqualTo("Offer not found");
        verify(offerRepository, times(1)).findById(nonExistentOfferId);
        verifyNoInteractions(offerMapper);
    }
}
