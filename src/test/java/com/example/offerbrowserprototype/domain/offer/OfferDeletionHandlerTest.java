//package com.example.offerbrowserprototype.domain.offer;
//
//import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//
//import static org.mockito.Mockito.*;
//
//class OfferDeletionHandlerTest {
//
//    private OfferRepository offerRepository;
//    private OfferDeletionHandler offerDeletionHandler;
//
//    @BeforeEach
//    void setUp() {
//        offerRepository = mock(OfferRepository.class);
//        offerDeletionHandler = new OfferDeletionHandler(offerRepository);
//    }
//
//    @Test
//    void shouldDeleteOfferById() {
//        // Given
//        String offerId = "123";
//
//        // When
//        offerDeletionHandler.deleteOffer(offerId);
//
//        // Then
//        verify(offerRepository, times(1)).deleteById(offerId);
//    }
//
//    @Test
//    void shouldNotThrowExceptionForNonExistentId() {
//        // Given
//        String nonExistentOfferId = "non-existent-id";
//        doNothing().when(offerRepository).deleteById(nonExistentOfferId);
//
//        // When
//        offerDeletionHandler.deleteOffer(nonExistentOfferId);
//
//        // Then
//        verify(offerRepository, times(1)).deleteById(nonExistentOfferId);
//    }
//}
