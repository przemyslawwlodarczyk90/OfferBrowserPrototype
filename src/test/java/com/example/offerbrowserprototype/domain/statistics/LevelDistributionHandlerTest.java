//package com.example.offerbrowserprototype.domain.statistics;
//
//import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//class LevelDistributionHandlerTest {
//
//    @Mock
//    private OfferRepository offerRepository;
//
//    private LevelDistributionHandler levelDistributionHandler;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        levelDistributionHandler = new LevelDistributionHandler(offerRepository);
//    }
//
//    @Test
//    void shouldReturnLevelDistributionSuccessfully() {
//        // Given
//        List<LevelDistribution> mockData = List.of(
//                new LevelDistribution("Junior", 10),
//                new LevelDistribution("Mid-Level", 20),
//                new LevelDistribution("Senior", 15)
//        );
//        when(offerRepository.getLevelDistributionSimple()).thenReturn(mockData);
//
//        // When
//        Map<String, Long> result = levelDistributionHandler.getLevelDistribution();
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result).hasSize(3);
//        assertThat(result).containsEntry("Junior", 10L);
//        assertThat(result).containsEntry("Mid-Level", 20L);
//        assertThat(result).containsEntry("Senior", 15L);
//    }
//
//    @Test
//    void shouldReturnEmptyMapWhenNoData() {
//        // Given
//        when(offerRepository.getLevelDistributionSimple()).thenReturn(List.of());
//
//        // When
//        Map<String, Long> result = levelDistributionHandler.getLevelDistribution();
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void shouldThrowExceptionWhenRepositoryFails() {
//        // Given
//        when(offerRepository.getLevelDistributionSimple()).thenThrow(new RuntimeException("Database error"));
//
//        // When & Then
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> levelDistributionHandler.getLevelDistribution());
//        assertThat(exception.getMessage()).isEqualTo("Database error");
//    }
//}
