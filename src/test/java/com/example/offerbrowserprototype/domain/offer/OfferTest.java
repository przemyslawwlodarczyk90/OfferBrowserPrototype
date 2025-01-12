package com.example.offerbrowserprototype.domain.offer;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OfferTest {

    @Test
    void shouldCreateOfferWithValidData() {
        // Given
        Clock clock = Clock.fixed(LocalDateTime.of(2025, 1, 1, 12, 0).toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));

        // When
        Offer offer = new Offer(
                "Software Engineer",
                "Developing backend services",
                "New York",
                "$100,000 - $120,000",
                "Mid-Level",
                "Tech Company",
                clock
        );

        // Then
        assertThat(offer.getTitle()).isEqualTo("Software Engineer");
        assertThat(offer.getDescription()).isEqualTo("Developing backend services");
        assertThat(offer.getLocation()).isEqualTo("New York");
        assertThat(offer.getSalaryRange()).isEqualTo("$100,000 - $120,000");
        assertThat(offer.getLevel()).isEqualTo("Mid-Level");
        assertThat(offer.getCompany()).isEqualTo("Tech Company");
        assertThat(offer.isDuplicate()).isFalse();
        assertThat(offer.getFetchedAt()).isEqualTo(LocalDateTime.of(2025, 1, 1, 12, 0));
    }

    @Test
    void shouldThrowExceptionWhenCompanyIsNull() {
        // Given
        Clock clock = Clock.systemUTC();

        // When & Then
        assertThatThrownBy(() -> new Offer(
                "Software Engineer",
                "Developing backend services",
                "New York",
                "$100,000 - $120,000",
                "Mid-Level",
                null,
                clock
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Company cannot be null or empty.");
    }

    @Test
    void shouldThrowExceptionWhenCompanyIsEmpty() {
        // Given
        Clock clock = Clock.systemUTC();

        // When & Then
        assertThatThrownBy(() -> new Offer(
                "Software Engineer",
                "Developing backend services",
                "New York",
                "$100,000 - $120,000",
                "Mid-Level",
                "  ",
                clock
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Company cannot be null or empty.");
    }

    @Test
    void shouldAllowUpdatingFields() {
        // Given
        Clock clock = Clock.systemUTC();
        Offer offer = new Offer(
                "Software Engineer",
                "Developing backend services",
                "New York",
                "$100,000 - $120,000",
                "Mid-Level",
                "Tech Company",
                clock
        );

        // When
        offer.setTitle("Senior Software Engineer");
        offer.setDuplicate(true);

        // Then
        assertThat(offer.getTitle()).isEqualTo("Senior Software Engineer");

        assertThat(offer.isDuplicate()).isTrue();
    }

    @Test
    void shouldGenerateDefaultFetchedAtForCurrentTime() {
        // Given
        Clock clock = Clock.systemDefaultZone();
        Offer offer = new Offer(
                "Software Engineer",
                "Developing backend services",
                "New York",
                "$100,000 - $120,000",
                "Mid-Level",
                "Tech Company",
                clock
        );

        // When
        LocalDateTime now = LocalDateTime.now(clock);

        // Then
        assertThat(offer.getFetchedAt()).isNotNull();
        assertThat(offer.getFetchedAt()).isBeforeOrEqualTo(now);
    }
}
