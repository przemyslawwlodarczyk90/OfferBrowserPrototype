package com.example.offerbrowserprototype.domain.statistics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LevelDistributionTest {

    @Test
    void shouldCreateLevelDistributionWithAllArgsConstructor() {
        // Given
        String id = "Mid-Level";
        long count = 10;

        // When
        LevelDistribution levelDistribution = new LevelDistribution(id, count);

        // Then
        assertThat(levelDistribution.getId()).isEqualTo(id);
        assertThat(levelDistribution.getCount()).isEqualTo(count);
    }

    @Test
    void shouldSetAndGetValues() {
        // Given
        LevelDistribution levelDistribution = new LevelDistribution();

        // When
        levelDistribution.setId("Senior");
        levelDistribution.setCount(25);

        // Then
        assertThat(levelDistribution.getId()).isEqualTo("Senior");
        assertThat(levelDistribution.getCount()).isEqualTo(25);
    }

    @Test
    void shouldEqualTwoIdenticalLevelDistributions() {
        // Given
        LevelDistribution levelDistribution1 = new LevelDistribution("Junior", 15);
        LevelDistribution levelDistribution2 = new LevelDistribution("Junior", 15);

        // Then
        assertThat(levelDistribution1).isEqualTo(levelDistribution2);
        assertThat(levelDistribution1.hashCode()).isEqualTo(levelDistribution2.hashCode());
    }

    @Test
    void shouldNotEqualDifferentLevelDistributions() {
        // Given
        LevelDistribution levelDistribution1 = new LevelDistribution("Junior", 15);
        LevelDistribution levelDistribution2 = new LevelDistribution("Senior", 20);

        // Then
        assertThat(levelDistribution1).isNotEqualTo(levelDistribution2);
        assertThat(levelDistribution1.hashCode()).isNotEqualTo(levelDistribution2.hashCode());
    }
}
