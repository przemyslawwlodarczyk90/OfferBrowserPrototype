package com.example.offerbrowserprototype.domain.statistics;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CityDistributionHandlerTest {

    @Mock
    private OfferRepository offerRepository;

    private CityDistributionHandler cityDistributionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cityDistributionHandler = new CityDistributionHandler(offerRepository);
    }

    @Test
    void shouldReturnProcessedCityDistribution() {
        // Mock data
        List<CityDistribution> mockDistribution = Arrays.asList(
                new CityDistribution("CityA, StateX", 5),
                new CityDistribution("CityB, StateY", 10),
                new CityDistribution("CityA, StateZ", 15)
        );

        // Mock behavior
        when(offerRepository.getCityDistributionSimple()).thenReturn(mockDistribution);

        // Execute
        Map<String, Long> result = cityDistributionHandler.getCityDistribution();

        // Verify
        assertThat(result).hasSize(2);
        assertThat(result.get("CityA")).isEqualTo(20); // 5 + 15
        assertThat(result.get("CityB")).isEqualTo(10);
    }

    @Test
    void shouldHandleEmptyDistribution() {
        // Mock behavior
        when(offerRepository.getCityDistributionSimple()).thenReturn(List.of());

        // Execute
        Map<String, Long> result = cityDistributionHandler.getCityDistribution();

        // Verify
        assertThat(result).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenRepositoryFails() {
        // Mock behavior
        when(offerRepository.getCityDistributionSimple()).thenThrow(new RuntimeException("Database error"));

        // Execute & Verify
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cityDistributionHandler.getCityDistribution();
        });

        assertThat(exception.getMessage()).isEqualTo("Database error");
    }
}
