package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDTO;
import com.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferAdditionHandlerTest {

    private OfferRepository offerRepository;
    private OfferAdditionHandler offerAdditionHandler;

    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        offerAdditionHandler = new OfferAdditionHandler(offerRepository);
    }

    @Test
    void addOffer() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(1L);
        offerDTO.setTitle("Test Title");
        offerDTO.setDescription("Test Description");
        offerDTO.setLocation("Test Location");
        offerDTO.setSalaryRange("Test Salary Range");
        offerDTO.setTechnologies("Test Technologies");

        Offer offer = new Offer();
        offer.setId(1L);
        offer.setTitle("Test Title");
        offer.setDescription("Test Description");
        offer.setLocation("Test Location");
        offer.setSalaryRange("Test Salary Range");
        offer.setTechnologies("Test Technologies");

        when(offerRepository.save(Mockito.any(Offer.class))).thenReturn(offer);

        OfferDTO result = offerAdditionHandler.addOffer(offerDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals("Test Location", result.getLocation());
        assertEquals("Test Salary Range", result.getSalaryRange());
        assertEquals("Test Technologies", result.getTechnologies());

        ArgumentCaptor<Offer> offerCaptor = ArgumentCaptor.forClass(Offer.class);
        verify(offerRepository).save(offerCaptor.capture());
        Offer capturedOffer = offerCaptor.getValue();

        assertEquals("Test Title", capturedOffer.getTitle());
        assertEquals("Test Description", capturedOffer.getDescription());
        assertEquals("Test Location", capturedOffer.getLocation());
        assertEquals("Test Salary Range", capturedOffer.getSalaryRange());
        assertEquals("Test Technologies", capturedOffer.getTechnologies());
    }
}