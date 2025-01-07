package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MarkAsDuplicateHandlerTest {

    private OfferRepository offerRepository;
    private MarkAsDuplicateHandler markAsDuplicateHandler;

    @BeforeEach
    void setUp() {
        offerRepository = Mockito.mock(OfferRepository.class);
        markAsDuplicateHandler = new MarkAsDuplicateHandler(offerRepository);
    }

    @Test
    void shouldMarkOfferAsDuplicateById() {
        // Given
        Offer offer = new Offer();
        offer.setId("offer123");
        offer.setDuplicate(false);
        when(offerRepository.findById("offer123")).thenReturn(Optional.of(offer));

        // When
        markAsDuplicateHandler.handleById("offer123");

        // Then
        assertThat(offer.isDuplicate()).isTrue();
        verify(offerRepository).save(offer);
    }

    @Test
    void shouldThrowExceptionWhenOfferNotFoundById() {
        // Given
        when(offerRepository.findById("nonexistentId")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> markAsDuplicateHandler.handleById("nonexistentId"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Offer with ID nonexistentId not found.");
        verify(offerRepository, never()).save(any(Offer.class));
    }

    @Test
    void shouldMarkOfferAsDuplicateByUrl() {
        // Given
        Offer offer = new Offer();
        offer.setOfferUrl("https://example.com/offer123");
        offer.setDuplicate(false);
        when(offerRepository.findByOfferUrl("https://example.com/offer123")).thenReturn(Optional.of(offer));

        // When
        markAsDuplicateHandler.handleByUrl("https://example.com/offer123");

        // Then
        assertThat(offer.isDuplicate()).isTrue();
        verify(offerRepository).save(offer);
    }

    @Test
    void shouldThrowExceptionWhenOfferNotFoundByUrl() {
        // Given
        when(offerRepository.findByOfferUrl("https://nonexistent.com/offer")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> markAsDuplicateHandler.handleByUrl("https://nonexistent.com/offer"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Offer with URL https://nonexistent.com/offer not found.");
        verify(offerRepository, never()).save(any(Offer.class));
    }

    @Test
    void shouldCallSaveMethodWhenOfferIsMarkedAsDuplicate() {
        // Given
        Offer offer = new Offer();
        offer.setId("offer123");
        offer.setDuplicate(false);
        when(offerRepository.findById("offer123")).thenReturn(Optional.of(offer));

        // When
        markAsDuplicateHandler.handleById("offer123");

        // Then
        verify(offerRepository, times(1)).save(offer);
    }
}
