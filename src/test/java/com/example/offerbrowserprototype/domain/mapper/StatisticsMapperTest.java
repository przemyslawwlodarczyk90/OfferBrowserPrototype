//package com.example.offerbrowserprototype.domain.mapper;
//
//import com.example.offerbrowserprototype.domain.dto.statistics.CityDistributionDTO;
//import com.example.offerbrowserprototype.domain.dto.statistics.LevelDistributionDTO;
//import com.example.offerbrowserprototype.domain.dto.statistics.StatisticsSummaryDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class StatisticsMapperTest {
//
//    private StatisticsMapper statisticsMapper;
//
//    @BeforeEach
//    void setUp() {
//        statisticsMapper = new StatisticsMapper();
//    }
//
//    @Test
//    void shouldMapToStatisticsSummaryDTO() {
//        // Given
//        long totalOffers = 100;
//
//
//        // When
//        StatisticsSummaryDTO dto = statisticsMapper.toDTO(totalOffers);
//
//        // Then
//        assertThat(dto).isNotNull();
//        assertThat(dto.getTotalOffers()).isEqualTo(totalOffers);
//
//    }
//
//    @Test
//    void shouldMapCityDistributionToDTOs() {
//        // Given
//        Map<String, Long> cityDistribution = Map.of(
//                "Warsaw", 50L,
//                "Krakow", 30L,
//                "Gdansk", 20L
//        );
//
//        // When
//        List<CityDistributionDTO> dtos = statisticsMapper.toCityDistributionDTOs(cityDistribution);
//
//        // Then
//        assertThat(dtos).isNotNull();
//        assertThat(dtos).hasSize(3);
//        assertThat(dtos).extracting("city").containsExactlyInAnyOrder("Warsaw", "Krakow", "Gdansk");
//        assertThat(dtos).extracting("count").containsExactlyInAnyOrder(50L, 30L, 20L);
//    }
//
//    @Test
//    void shouldMapLevelDistributionToDTOs() {
//        // Given
//        Map<String, Long> levelDistribution = Map.of(
//                "Junior", 40L,
//                "Mid", 35L,
//                "Senior", 25L
//        );
//
//        // When
//        List<LevelDistributionDTO> dtos = statisticsMapper.toLevelDistributionDTOs(levelDistribution);
//
//        // Then
//        assertThat(dtos).isNotNull();
//        assertThat(dtos).hasSize(3);
//        assertThat(dtos).extracting("level").containsExactlyInAnyOrder("Junior", "Mid", "Senior");
//        assertThat(dtos).extracting("count").containsExactlyInAnyOrder(40L, 35L, 25L);
//    }
//
//    @Test
//    void shouldHandleEmptyCityDistribution() {
//        // Given
//        Map<String, Long> cityDistribution = Map.of();
//
//        // When
//        List<CityDistributionDTO> dtos = statisticsMapper.toCityDistributionDTOs(cityDistribution);
//
//        // Then
//        assertThat(dtos).isNotNull();
//        assertThat(dtos).isEmpty();
//    }
//
//    @Test
//    void shouldHandleEmptyLevelDistribution() {
//        // Given
//        Map<String, Long> levelDistribution = Map.of();
//
//        // When
//        List<LevelDistributionDTO> dtos = statisticsMapper.toLevelDistributionDTOs(levelDistribution);
//
//        // Then
//        assertThat(dtos).isNotNull();
//        assertThat(dtos).isEmpty();
//    }
//}
