package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class OfferApplicationHandlerTest {

    private OfferRepository offerRepository;
    private OfferDetailsHandler offerDetailsHandler;
    private ApplicationNoteHandler applicationNoteHandler;
    private OfferApplicationHandler offerApplicationHandler;

    @BeforeEach
    void setUp() {
        offerRepository = Mockito.mock(OfferRepository.class);
        offerDetailsHandler = Mockito.mock(OfferDetailsHandler.class);
        applicationNoteHandler = Mockito.mock(ApplicationNoteHandler.class);
        offerApplicationHandler = new OfferApplicationHandler(offerRepository, offerDetailsHandler, applicationNoteHandler);
    }

    @Test
    void shouldApplyToOfferAndSaveApplicationNote() {
        // Given
        String offerId = "123";
        OfferDTO mockOfferDTO = new OfferDTO();
        mockOfferDTO.setOfferUrl("https://example.com/offer");
        mockOfferDTO.setCompany("Test Company");
        when(offerDetailsHandler.getOfferById(offerId)).thenReturn(mockOfferDTO);

        Offer mockOffer = new Offer();
        when(offerRepository.findById(offerId)).thenReturn(Optional.of(mockOffer));

        // When
        offerApplicationHandler.applyToOfferWithNote(offerId);

        // Then
        verify(offerDetailsHandler).getOfferById(offerId);
        verify(offerRepository).findById(offerId);
        verify(offerRepository).save(mockOffer);
        verify(applicationNoteHandler).saveApplicationNote(offerId, "https://example.com/offer", "Test Company");

        assertThat(mockOffer.isApplied()).isTrue();
    }

    @Test
    void shouldThrowExceptionIfOfferNotFound() {
        // Given
        String offerId = "123";
        when(offerDetailsHandler.getOfferById(offerId)).thenReturn(null);

        // When / Then
        assertThatThrownBy(() -> offerApplicationHandler.applyToOfferWithNote(offerId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Offer not found for ID: " + offerId);

        verify(offerDetailsHandler).getOfferById(offerId);
        verifyNoInteractions(offerRepository);
        verifyNoInteractions(applicationNoteHandler);
    }

    @Test
    void shouldThrowExceptionIfOfferDetailsAreIncomplete() {
        // Given
        String offerId = "123";
        OfferDTO incompleteOffer = new OfferDTO();
        incompleteOffer.setCompany(null);
        incompleteOffer.setOfferUrl(null);

        when(offerDetailsHandler.getOfferById(offerId)).thenReturn(incompleteOffer);

        // When / Then
        assertThatThrownBy(() -> offerApplicationHandler.applyToOfferWithNote(offerId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Offer details are incomplete. Cannot save application note.");

        verify(offerDetailsHandler).getOfferById(offerId);
        verifyNoInteractions(offerRepository);
        verifyNoInteractions(applicationNoteHandler);
    }

    @Test
    void shouldThrowExceptionIfOfferEntityNotFound() {
        // Given
        String offerId = "123";
        OfferDTO mockOfferDTO = new OfferDTO();
        mockOfferDTO.setOfferUrl("https://example.com/offer");
        mockOfferDTO.setCompany("Test Company");

        when(offerDetailsHandler.getOfferById(offerId)).thenReturn(mockOfferDTO);
        when(offerRepository.findById(offerId)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> offerApplicationHandler.applyToOfferWithNote(offerId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Offer not found");

        verify(offerDetailsHandler).getOfferById(offerId);
        verify(offerRepository).findById(offerId);
        verifyNoInteractions(applicationNoteHandler);
    }
}
