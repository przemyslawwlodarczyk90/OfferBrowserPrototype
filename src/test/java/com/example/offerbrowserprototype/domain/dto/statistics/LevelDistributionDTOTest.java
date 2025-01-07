package com.example.offerbrowserprototype.domain.dto.statistics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LevelDistributionDTOTest {

    @Test
    void shouldCreateLevelDistributionDTOWithAllArgsConstructor() {
        // Given & When
        LevelDistributionDTO levelDistribution = new LevelDistributionDTO("Junior", 150);

        // Then
        assertThat(levelDistribution.getLevel()).isEqualTo("Junior");
        assertThat(levelDistribution.getCount()).isEqualTo(150);
    }

    @Test
    void shouldSetAndGetFieldsCorrectly() {
        // Given
        LevelDistributionDTO levelDistribution = new LevelDistributionDTO("Mid", 80);

        // When
        levelDistribution.setLevel("Senior");
        levelDistribution.setCount(200);

        // Then
        assertThat(levelDistribution.getLevel()).isEqualTo("Senior");
        assertThat(levelDistribution.getCount()).isEqualTo(200);
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        // Given
        LevelDistributionDTO dto1 = new LevelDistributionDTO("Junior", 150);
        LevelDistributionDTO dto2 = new LevelDistributionDTO("Junior", 150);

        // Then
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void shouldTestNotEquals() {
        // Given
        LevelDistributionDTO dto1 = new LevelDistributionDTO("Junior", 150);
        LevelDistributionDTO dto2 = new LevelDistributionDTO("Mid", 100);

        // Then
        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    void shouldHandleNullLevelField() {
        // Given & When
        LevelDistributionDTO levelDistribution = new LevelDistributionDTO(null, 75);

        // Then
        assertThat(levelDistribution.getLevel()).isNull();
        assertThat(levelDistribution.getCount()).isEqualTo(75);
    }
}
