package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDTO;
import com.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferRetrievalHandlerTest {

    private OfferRepository offerRepository;
    private OfferRetrievalHandler offerRetrievalHandler;

    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        offerRetrievalHandler = new OfferRetrievalHandler(offerRepository);
    }

    @Test
    void getOffer() {
        Long id = 1L;
        Offer offer = new Offer(id, "Test Title", "Test Description", "Test Location", "Test Salary Range", "Test Technologies");
        when(offerRepository.findById(id)).thenReturn(Optional.of(offer));

        OfferDTO result = offerRetrievalHandler.getOffer(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals("Test Location", result.getLocation());
        assertEquals("Test Salary Range", result.getSalaryRange());
        assertEquals("Test Technologies", result.getTechnologies());

        verify(offerRepository, times(1)).findById(id);
    }

    @Test
    void getOfferNotFound() {
        Long id = 1L;
        when(offerRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            offerRetrievalHandler.getOffer(id);
        });

        assertEquals("Offer not found", exception.getMessage());
        verify(offerRepository, times(1)).findById(id);
    }

    @Test
    void getAllOffers() {
        Offer offer1 = new Offer(1L, "Title 1", "Description 1", "Location 1", "Salary Range 1", "Technologies 1");
        Offer offer2 = new Offer(2L, "Title 2", "Description 2", "Location 2", "Salary Range 2", "Technologies 2");
        List<Offer> offers = Arrays.asList(offer1, offer2);

        when(offerRepository.findAll()).thenReturn(offers);

        List<OfferDTO> result = offerRetrievalHandler.getAllOffers();

        assertNotNull(result);
        assertEquals(2, result.size());

        OfferDTO offerDTO1 = result.get(0);
        assertEquals(1L, offerDTO1.getId());
        assertEquals("Title 1", offerDTO1.getTitle());
        assertEquals("Description 1", offerDTO1.getDescription());
        assertEquals("Location 1", offerDTO1.getLocation());
        assertEquals("Salary Range 1", offerDTO1.getSalaryRange());
        assertEquals("Technologies 1", offerDTO1.getTechnologies());

        OfferDTO offerDTO2 = result.get(1);
        assertEquals(2L, offerDTO2.getId());
        assertEquals("Title 2", offerDTO2.getTitle());
        assertEquals("Description 2", offerDTO2.getDescription());
        assertEquals("Location 2", offerDTO2.getLocation());
        assertEquals("Salary Range 2", offerDTO2.getSalaryRange());
        assertEquals("Technologies 2", offerDTO2.getTechnologies());

        verify(offerRepository, times(1)).findAll();
    }
}
