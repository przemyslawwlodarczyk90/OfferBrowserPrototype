package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class OfferDeletionHandlerTest {

    private OfferRepository offerRepository;
    private OfferDeletionHandler offerDeletionHandler;

    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        offerDeletionHandler = new OfferDeletionHandler(offerRepository);
    }

    @Test
    void deleteOffer() {
        Long id = 1L;

        offerDeletionHandler.deleteOffer(id);

        verify(offerRepository, times(1)).deleteById(id);
    }
}
