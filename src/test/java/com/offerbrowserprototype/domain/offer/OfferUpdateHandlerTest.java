package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDTO;
import com.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferUpdateHandlerTest {

    private OfferRepository offerRepository;
    private OfferUpdateHandler offerUpdateHandler;

    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        offerUpdateHandler = new OfferUpdateHandler(offerRepository);
    }

    @Test
    void updateOffer() {
        Long id = 1L;
        OfferDTO offerDTO = new OfferDTO(id, "Updated Title", "Updated Description", "Updated Location", "Updated Salary Range", "Updated Technologies");

        Offer offer = new Offer(id, "Old Title", "Old Description", "Old Location", "Old Salary Range", "Old Technologies");
        Offer updatedOffer = new Offer(id, "Updated Title", "Updated Description", "Updated Location", "Updated Salary Range", "Updated Technologies");

        when(offerRepository.findById(id)).thenReturn(Optional.of(offer));
        when(offerRepository.save(any(Offer.class))).thenReturn(updatedOffer);

        OfferDTO result = offerUpdateHandler.updateOffer(id, offerDTO);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals("Updated Location", result.getLocation());
        assertEquals("Updated Salary Range", result.getSalaryRange());
        assertEquals("Updated Technologies", result.getTechnologies());

        ArgumentCaptor<Offer> offerCaptor = ArgumentCaptor.forClass(Offer.class);
        verify(offerRepository).save(offerCaptor.capture());
        Offer capturedOffer = offerCaptor.getValue();

        assertEquals("Updated Title", capturedOffer.getTitle());
        assertEquals("Updated Description", capturedOffer.getDescription());
        assertEquals("Updated Location", capturedOffer.getLocation());
        assertEquals("Updated Salary Range", capturedOffer.getSalaryRange());
        assertEquals("Updated Technologies", capturedOffer.getTechnologies());
    }

    @Test
    void updateOfferNotFound() {
        Long id = 1L;
        OfferDTO offerDTO = new OfferDTO(id, "Updated Title", "Updated Description", "Updated Location", "Updated Salary Range", "Updated Technologies");

        when(offerRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            offerUpdateHandler.updateOffer(id, offerDTO);
        });

        assertEquals("Offer not found", exception.getMessage());
        verify(offerRepository, times(1)).findById(id);
        verify(offerRepository, times(0)).save(any(Offer.class));
    }
}