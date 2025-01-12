//package com.example.offerbrowserprototype.domain.dto.statistics;
//
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class StatisticsSummaryDTOTest {
//
//    @Test
//    void shouldCreateStatisticsSummaryDTOWithAllArgsConstructor() {
//        // Given & When
//        StatisticsSummaryDTO summary = new StatisticsSummaryDTO(100, 25);
//
//        // Then
//        assertThat(summary.getTotalOffers()).isEqualTo(100);
//        assertThat(summary.getAppliedOffers()).isEqualTo(25);
//    }
//
//    @Test
//    void shouldSetAndGetFieldsCorrectly() {
//        // Given
//        StatisticsSummaryDTO summary = new StatisticsSummaryDTO(200, 50);
//
//        // When
//        summary.setTotalOffers(300);
//        summary.setAppliedOffers(75);
//
//        // Then
//        assertThat(summary.getTotalOffers()).isEqualTo(300);
//        assertThat(summary.getAppliedOffers()).isEqualTo(75);
//    }
//
//    @Test
//    void shouldTestEqualsAndHashCode() {
//        // Given
//        StatisticsSummaryDTO dto1 = new StatisticsSummaryDTO(100, 50);
//        StatisticsSummaryDTO dto2 = new StatisticsSummaryDTO(100, 50);
//
//        // Then
//        assertThat(dto1).isEqualTo(dto2);
//        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
//    }
//
//    @Test
//    void shouldTestNotEquals() {
//        // Given
//        StatisticsSummaryDTO dto1 = new StatisticsSummaryDTO(100, 50);
//        StatisticsSummaryDTO dto2 = new StatisticsSummaryDTO(200, 75);
//
//        // Then
//        assertThat(dto1).isNotEqualTo(dto2);
//    }
//
//    @Test
//    void shouldHandleZeroValues() {
//        // Given & When
//        StatisticsSummaryDTO summary = new StatisticsSummaryDTO(0, 0);
//
//        // Then
//        assertThat(summary.getTotalOffers()).isEqualTo(0);
//        assertThat(summary.getAppliedOffers()).isEqualTo(0);
//    }
//}
