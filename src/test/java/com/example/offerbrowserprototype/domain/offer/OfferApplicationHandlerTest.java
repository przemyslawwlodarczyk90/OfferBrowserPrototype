//package com.example.offerbrowserprototype.domain.offer;
//
//import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//
///**
// * Testy jednostkowe dla klasy {@link OfferApplicationHandler}.
// */
//class OfferApplicationHandlerTest {

//    @Mock
//    private OfferRepository offerRepository;
//
//    private OfferApplicationHandler offerApplicationHandler;
//
//    /**
//     * Inicjalizacja zasobów przed każdym testem.
//     */
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        offerApplicationHandler = new OfferApplicationHandler(offerRepository);
//    }
//
//    /**
//     * Test sprawdzający poprawne oznaczenie oferty jako aplikowanej.
//     */
//    @Test
//    void shouldMarkOfferAsAppliedSuccessfully() {
//        // Given - Dane testowe
//        String offerId = "1";
//        Offer offer = new Offer();
//        offer.setId(offerId);
//        offer.setApplied(false);
//
//        Mockito.when(offerRepository.findById(offerId)).thenReturn(Optional.of(offer));
//        Mockito.when(offerRepository.save(any(Offer.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // When - Wywołanie metody
//        offerApplicationHandler.applyToOffer(offerId);
//
//        // Then - Sprawdzenie wyników
//        assertTrue(offer.isApplied(), "Oferta powinna zostać oznaczona jako aplikowana.");
//        Mockito.verify(offerRepository).save(offer);
//    }
//
//    /**
//     * Test sprawdzający rzucenie wyjątku, gdy oferta nie istnieje.
//     */
//    @Test
//    void shouldThrowExceptionWhenOfferNotFound() {
//        // Given - Dane testowe
//        String offerId = "non-existent";
//
//        Mockito.when(offerRepository.findById(offerId)).thenReturn(Optional.empty());
//
//        // When & Then - Wywołanie metody i oczekiwanie wyjątku
//        assertThrows(IllegalArgumentException.class,
//                () -> offerApplicationHandler.applyToOffer(offerId),
//                "Powinien zostać rzucony wyjątek, gdy oferta nie istnieje.");
//    }
//}
