package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OfferRetrievalHandlerTest {

    private OfferRepository offerRepository;
    private OfferMapper offerMapper;
    private OfferRetrievalHandler offerRetrievalHandler;

    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        offerMapper = mock(OfferMapper.class);
        offerRetrievalHandler = new OfferRetrievalHandler(offerRepository, offerMapper);
    }

    @Test
    void shouldReturnOfferWhenIdExists() {
        // Mock danych
        String offerId = "123";
        Offer mockOffer = new Offer();
        OfferDTO mockOfferDTO = new OfferDTO();

        when(offerRepository.findById(offerId)).thenReturn(Optional.of(mockOffer));
        when(offerMapper.toDTO(mockOffer)).thenReturn(mockOfferDTO);

        // Wywołanie metody
        OfferDTO result = offerRetrievalHandler.getOffer(offerId);

        // Weryfikacja wyników
        assertThat(result).isNotNull();
        verify(offerRepository, times(1)).findById(offerId);
        verify(offerMapper, times(1)).toDTO(mockOffer);
    }

    @Test
    void shouldReturnNullWhenIdDoesNotExist() {
        // Mock danych
        String offerId = "123";

        when(offerRepository.findById(offerId)).thenReturn(Optional.empty());

        // Wywołanie metody
        OfferDTO result = offerRetrievalHandler.getOffer(offerId);

        // Weryfikacja wyników
        assertThat(result).isNull();
        verify(offerRepository, times(1)).findById(offerId);
        verifyNoInteractions(offerMapper);
    }
}
