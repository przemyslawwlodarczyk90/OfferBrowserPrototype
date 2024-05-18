package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferFacadeTest {

    private OfferAdditionHandler additionHandler;
    private OfferUpdateHandler updateHandler;
    private OfferDeletionHandler deletionHandler;
    private OfferRetrievalHandler retrievalHandler;
    private OfferFacade offerFacade;

    @BeforeEach
    void setUp() {
        additionHandler = mock(OfferAdditionHandler.class);
        updateHandler = mock(OfferUpdateHandler.class);
        deletionHandler = mock(OfferDeletionHandler.class);
        retrievalHandler = mock(OfferRetrievalHandler.class);
        offerFacade = new OfferFacade(additionHandler, updateHandler, deletionHandler, retrievalHandler);
    }

    @Test
    void addOffer() {
        OfferDTO offerDTO = new OfferDTO(1L, "Test Title", "Test Description", "Test Location", "Test Salary Range", "Test Technologies");
        when(additionHandler.addOffer(offerDTO)).thenReturn(offerDTO);

        OfferDTO result = offerFacade.addOffer(offerDTO);

        assertNotNull(result);
        assertEquals(offerDTO, result);
        verify(additionHandler).addOffer(offerDTO);
    }

    @Test
    void updateOffer() {
        Long id = 1L;
        OfferDTO offerDTO = new OfferDTO(id, "Updated Title", "Updated Description", "Updated Location", "Updated Salary Range", "Updated Technologies");
        when(updateHandler.updateOffer(id, offerDTO)).thenReturn(offerDTO);

        OfferDTO result = offerFacade.updateOffer(id, offerDTO);

        assertNotNull(result);
        assertEquals(offerDTO, result);
        verify(updateHandler).updateOffer(id, offerDTO);
    }

    @Test
    void deleteOffer() {
        Long id = 1L;

        offerFacade.deleteOffer(id);

        verify(deletionHandler).deleteOffer(id);
    }

    @Test
    void getOffer() {
        Long id = 1L;
        OfferDTO offerDTO = new OfferDTO(id, "Test Title", "Test Description", "Test Location", "Test Salary Range", "Test Technologies");
        when(retrievalHandler.getOffer(id)).thenReturn(offerDTO);

        OfferDTO result = offerFacade.getOffer(id);

        assertNotNull(result);
        assertEquals(offerDTO, result);
        verify(retrievalHandler).getOffer(id);
    }

    @Test
    void getAllOffers() {
        List<OfferDTO> offerDTOList = Arrays.asList(
                new OfferDTO(1L, "Title 1", "Description 1", "Location 1", "Salary Range 1", "Technologies 1"),
                new OfferDTO(2L, "Title 2", "Description 2", "Location 2", "Salary Range 2", "Technologies 2")
        );
        when(retrievalHandler.getAllOffers()).thenReturn(offerDTOList);

        List<OfferDTO> result = offerFacade.getAllOffers();

        assertNotNull(result);
        assertEquals(offerDTOList, result);
        verify(retrievalHandler).getAllOffers();
    }
}