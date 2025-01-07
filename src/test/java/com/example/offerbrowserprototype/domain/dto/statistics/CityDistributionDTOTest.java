package com.example.offerbrowserprototype.domain.dto.statistics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CityDistributionDTOTest {

    @Test
     void shouldCreateCityDistributionDTOWithAllArgsConstructor() {
        // Given & When
        CityDistributionDTO cityDistribution = new CityDistributionDTO("Warsaw", 120);

        // Then
        assertThat(cityDistribution.getCity()).isEqualTo("Warsaw");
        assertThat(cityDistribution.getCount()).isEqualTo(120);
    }

    @Test
     void shouldSetAndGetFieldsCorrectly() {
        // Given
        CityDistributionDTO cityDistribution = new CityDistributionDTO("Krakow", 75);

        // When
        cityDistribution.setCity("Gdansk");
        cityDistribution.setCount(90);

        // Then
        assertThat(cityDistribution.getCity()).isEqualTo("Gdansk");
        assertThat(cityDistribution.getCount()).isEqualTo(90);
    }

    @Test
     void shouldTestEqualsAndHashCode() {
        // Given
        CityDistributionDTO dto1 = new CityDistributionDTO("Warsaw", 120);
        CityDistributionDTO dto2 = new CityDistributionDTO("Warsaw", 120);

        // Then
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
     void shouldTestNotEquals() {
        // Given
        CityDistributionDTO dto1 = new CityDistributionDTO("Warsaw", 120);
        CityDistributionDTO dto2 = new CityDistributionDTO("Krakow", 75);

        // Then
        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
     void shouldHandleNullCityField() {
        // Given & When
        CityDistributionDTO cityDistribution = new CityDistributionDTO(null, 50);

        // Then
        assertThat(cityDistribution.getCity()).isNull();
        assertThat(cityDistribution.getCount()).isEqualTo(50);
    }
}
