package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OfferAppliedListHandlerTest {

    private OfferRepository offerRepository;
    private OfferMapper offerMapper;
    private OfferAppliedListHandler offerAppliedListHandler;

    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        offerMapper = mock(OfferMapper.class);
        offerAppliedListHandler = new OfferAppliedListHandler(offerRepository, offerMapper);
    }

    @Test
    void shouldReturnAppliedOffers() {
        // Given
        Offer offer1 = new Offer();
        offer1.setId("1");
        offer1.setTitle("Java Developer");
        offer1.setApplied(true);
        offer1.setFetchedAt(LocalDateTime.now().minusDays(1));

        Offer offer2 = new Offer();
        offer2.setId("2");
        offer2.setTitle("Backend Developer");
        offer2.setApplied(true);
        offer2.setFetchedAt(LocalDateTime.now());

        OfferDTO offerDTO1 = new OfferDTO("1", "Java Developer", "Develop Java apps", "Location 1", "url1", "5000-7000", "Company A", "Mid", true, offer1.getFetchedAt());
        OfferDTO offerDTO2 = new OfferDTO("2", "Backend Developer", "Develop Backend apps", "Location 2", "url2", "6000-8000", "Company B", "Senior", true, offer2.getFetchedAt());

        when(offerRepository.findByAppliedTrueOrderByFetchedAtDesc()).thenReturn(Arrays.asList(offer2, offer1));
        when(offerMapper.toDTO(offer1)).thenReturn(offerDTO1);
        when(offerMapper.toDTO(offer2)).thenReturn(offerDTO2);

        // When
        List<OfferDTO> appliedOffers = offerAppliedListHandler.getAppliedOffers();

        // Then
        assertThat(appliedOffers).hasSize(2);
        assertThat(appliedOffers.get(0).getId()).isEqualTo("2");
        assertThat(appliedOffers.get(1).getId()).isEqualTo("1");

        Mockito.verify(offerRepository).findByAppliedTrueOrderByFetchedAtDesc();
        Mockito.verify(offerMapper).toDTO(offer1);
        Mockito.verify(offerMapper).toDTO(offer2);
    }

    @Test
    void shouldReturnEmptyListWhenNoAppliedOffers() {
        // Given
        when(offerRepository.findByAppliedTrueOrderByFetchedAtDesc()).thenReturn(List.of());

        // When
        List<OfferDTO> appliedOffers = offerAppliedListHandler.getAppliedOffers();

        // Then
        assertThat(appliedOffers).isEmpty();
        Mockito.verify(offerRepository).findByAppliedTrueOrderByFetchedAtDesc();
        Mockito.verifyNoInteractions(offerMapper);
    }
}
