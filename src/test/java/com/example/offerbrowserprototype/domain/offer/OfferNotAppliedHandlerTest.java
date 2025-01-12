//package com.example.offerbrowserprototype.domain.offer;
//
//import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
//import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
//import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//class OfferNotAppliedHandlerTest {
//
//    private OfferRepository offerRepository;
//    private OfferMapper offerMapper;
//    private OfferNotAppliedHandler offerNotAppliedHandler;
//
//    @BeforeEach
//    void setUp() {
//        offerRepository = mock(OfferRepository.class);
//        offerMapper = mock(OfferMapper.class);
//        offerNotAppliedHandler = new OfferNotAppliedHandler(offerRepository, offerMapper);
//    }
//
//    @Test
//    void shouldReturnNotAppliedOffers() {
//        // Przygotowanie danych testowych
//        Offer offer1 = new Offer();
//        offer1.setId("1");
//        offer1.setTitle("Offer 1");
//        offer1.setApplied(false);
//        offer1.setFetchedAt(LocalDateTime.now().minusDays(1));
//
//        Offer offer2 = new Offer();
//        offer2.setId("2");
//        offer2.setTitle("Offer 2");
//        offer2.setApplied(false);
//        offer2.setFetchedAt(LocalDateTime.now());
//
//        List<Offer> mockOffers = Arrays.asList(offer2, offer1);
//
//        OfferDTO dto1 = new OfferDTO();
//        dto1.setId("1");
//        dto1.setTitle("Offer 1");
//
//        OfferDTO dto2 = new OfferDTO();
//        dto2.setId("2");
//        dto2.setTitle("Offer 2");
//
//        // Mockowanie zachowania
//        when(offerRepository.findByAppliedFalseOrderByFetchedAtDesc()).thenReturn(mockOffers);
//        when(offerMapper.toDTO(offer1)).thenReturn(dto1);
//        when(offerMapper.toDTO(offer2)).thenReturn(dto2);
//
//        // Wywołanie metody
//        List<OfferDTO> result = offerNotAppliedHandler.getNotAppliedOffers();
//
//        // Weryfikacja
//        verify(offerRepository, times(1)).findByAppliedFalseOrderByFetchedAtDesc();
//        verify(offerMapper, times(2)).toDTO(any(Offer.class));
//
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getId()).isEqualTo("2");
//        assertThat(result.get(1).getId()).isEqualTo("1");
//    }
//}
