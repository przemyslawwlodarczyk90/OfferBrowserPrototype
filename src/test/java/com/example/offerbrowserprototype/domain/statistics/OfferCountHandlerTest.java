package com.example.offerbrowserprototype.domain.statistics;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class OfferCountHandlerTest {

    @Mock
    private OfferRepository offerRepository;

    private OfferCountHandler offerCountHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        offerCountHandler = new OfferCountHandler(offerRepository);
    }

    @Test
    void shouldReturnTotalOffersCount() {
        // Given
        long mockTotalOffers = 100L;
        when(offerRepository.countByIsDuplicateFalse()).thenReturn(mockTotalOffers);

        // When
        long totalOffers = offerCountHandler.getTotalOffers();

        // Then
        assertThat(totalOffers).isEqualTo(mockTotalOffers);
    }

    @Test
    void shouldReturnAppliedOffersCount() {
        // Given
        long mockAppliedOffers = 50L;
        when(offerRepository.countByAppliedTrue()).thenReturn(mockAppliedOffers);

        // When
        long appliedOffers = offerCountHandler.getAppliedOffers();

        // Then
        assertThat(appliedOffers).isEqualTo(mockAppliedOffers);
    }

    @Test
    void shouldReturnZeroWhenNoTotalOffers() {
        // Given
        when(offerRepository.countByIsDuplicateFalse()).thenReturn(0L);

        // When
        long totalOffers = offerCountHandler.getTotalOffers();

        // Then
        assertThat(totalOffers).isEqualTo(0L);
    }

    @Test
    void shouldReturnZeroWhenNoAppliedOffers() {
        // Given
        when(offerRepository.countByAppliedTrue()).thenReturn(0L);

        // When
        long appliedOffers = offerCountHandler.getAppliedOffers();

        // Then
        assertThat(appliedOffers).isEqualTo(0L);
    }
}
