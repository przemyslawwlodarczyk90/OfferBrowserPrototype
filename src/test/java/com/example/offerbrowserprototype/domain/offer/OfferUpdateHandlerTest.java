package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OfferUpdateHandlerTest {

    private OfferRepository offerRepository;
    private OfferMapper offerMapper;
    private Clock clock;
    private OfferUpdateHandler offerUpdateHandler;

    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        offerMapper = mock(OfferMapper.class);
        clock = Clock.fixed(Instant.parse("2025-01-01T10:00:00Z"), ZoneId.of("UTC"));
        offerUpdateHandler = new OfferUpdateHandler(offerRepository, offerMapper, clock);
    }

    @Test
    void shouldUpdateOfferSuccessfully() {
        // Mock danych
        String offerId = "123";
        Offer existingOffer = new Offer();
        existingOffer.setId(offerId);
        existingOffer.setTitle("Old Title");

        OfferDTO updatedOfferDto = new OfferDTO();
        updatedOfferDto.setTitle("New Title");
        updatedOfferDto.setDescription("Updated Description");
        updatedOfferDto.setLocation("Updated Location");
        updatedOfferDto.setSalaryRange("Updated Salary");
        updatedOfferDto.setLevel("Senior");

        Offer updatedOffer = new Offer();
        updatedOffer.setId(offerId);
        updatedOffer.setTitle("New Title");
        updatedOffer.setDescription("Updated Description");
        updatedOffer.setLocation("Updated Location");
        updatedOffer.setSalaryRange("Updated Salary");
        updatedOffer.setLevel("Senior");
        updatedOffer.setFetchedAt(LocalDateTime.now(clock));

        OfferDTO updatedOfferDtoResponse = new OfferDTO();
        updatedOfferDtoResponse.setTitle("New Title");

        when(offerRepository.findById(offerId)).thenReturn(Optional.of(existingOffer));
        when(offerRepository.save(existingOffer)).thenReturn(updatedOffer);
        when(offerMapper.toDTO(updatedOffer)).thenReturn(updatedOfferDtoResponse);

        // Wywołanie metody
        OfferDTO result = offerUpdateHandler.updateOffer(offerId, updatedOfferDto);

        // Weryfikacja wyników
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New Title");
        verify(offerRepository, times(1)).findById(offerId);
        verify(offerRepository, times(1)).save(existingOffer);
        verify(offerMapper, times(1)).toDTO(updatedOffer);
    }

    @Test
    void shouldThrowExceptionWhenOfferNotFound() {
        // Mock danych
        String offerId = "123";
        OfferDTO offerDto = new OfferDTO();

        when(offerRepository.findById(offerId)).thenReturn(Optional.empty());

        // Wywołanie metody i weryfikacja
        Exception exception = assertThrows(RuntimeException.class, () -> {
            offerUpdateHandler.updateOffer(offerId, offerDto);
        });

        assertThat(exception.getMessage()).isEqualTo("Offer not found");
        verify(offerRepository, times(1)).findById(offerId);
        verifyNoInteractions(offerMapper);
    }
}
