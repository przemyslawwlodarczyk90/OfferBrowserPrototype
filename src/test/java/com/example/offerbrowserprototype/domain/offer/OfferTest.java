package com.example.offerbrowserprototype.domain.offer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe dla klasy {@link Offer}.
 */
class OfferTest {

    private Clock fixedClock;

    /**
     * Inicjalizacja stałego zegara (Clock) dla spójnych testów.
     */
    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2024-01-01T12:00:00Z"), ZoneId.of("UTC"));
    }

    /**
     * Test sprawdzający, czy konstruktor z parametrami poprawnie przypisuje wartości.
     */
    @Test
    void shouldCreateOfferWithGivenValues() {
        // Given - Dane testowe
        String title = "Software Developer";
        String description = "Develop innovative software solutions.";
        String location = "Warsaw";
        String salaryRange = "10,000 - 15,000 PLN";
        String technologies = "Java, Spring Boot";

        // When - Wywołanie konstruktora
        Offer offer = new Offer(title, description, location, salaryRange, technologies, fixedClock);

        // Then - Sprawdzenie wyników
        assertNull(offer.getId(), "ID powinno być null przy tworzeniu nowej oferty.");
        assertEquals(title, offer.getTitle(), "Tytuł powinien być zgodny z podanym.");
        assertEquals(description, offer.getDescription(), "Opis powinien być zgodny z podanym.");
        assertEquals(location, offer.getLocation(), "Lokalizacja powinna być zgodna z podaną.");
        assertEquals(salaryRange, offer.getSalaryRange(), "Zakres wynagrodzenia powinien być zgodny z podanym.");
        assertEquals(technologies, offer.getLevel(), "Technologie powinny być zgodne z podanymi.");
        assertFalse(offer.isApplied(), "Początkowa wartość applied powinna być false.");
        assertEquals(LocalDateTime.now(fixedClock), offer.getFetchedAt(), "Czas fetchedAt powinien być zgodny z czasem utworzonym przez Clock.");
    }

    /**
     * Test sprawdzający ustawienie i pobranie wartości pól za pomocą setterów i getterów.
     */
    @Test
    void shouldSetAndGetFieldsCorrectly() {
        // Given - Nowy obiekt Offer
        Offer offer = new Offer();

        // When - Ustawianie wartości pól
        offer.setId("123");
        offer.setTitle("Data Scientist");
        offer.setDescription("Analyze and model complex datasets.");
        offer.setLocation("Krakow");
        offer.setSalaryRange("12,000 - 18,000 PLN");
        offer.setLevel("Python, Machine Learning");
        offer.setApplied(true);
        offer.setFetchedAt(LocalDateTime.of(2024, 1, 2, 10, 0));

        // Then - Sprawdzanie wyników
        assertEquals("123", offer.getId(), "ID powinno być zgodne z ustawionym.");
        assertEquals("Data Scientist", offer.getTitle(), "Tytuł powinien być zgodny z ustawionym.");
        assertEquals("Analyze and model complex datasets.", offer.getDescription(), "Opis powinien być zgodny z ustawionym.");
        assertEquals("Krakow", offer.getLocation(), "Lokalizacja powinna być zgodna z ustawioną.");
        assertEquals("12,000 - 18,000 PLN", offer.getSalaryRange(), "Zakres wynagrodzenia powinien być zgodny z ustawionym.");
        assertEquals("Python, Machine Learning", offer.getLevel(), "Technologie powinny być zgodne z ustawionymi.");
        assertTrue(offer.isApplied(), "Wartość applied powinna być true.");
        assertEquals(LocalDateTime.of(2024, 1, 2, 10, 0), offer.getFetchedAt(), "Czas fetchedAt powinien być zgodny z ustawionym.");
    }

    /**
     * Test sprawdzający domyślne wartości pól w konstruktorze bez parametrów.
     */
    @Test
    void shouldHaveDefaultValuesWithNoArgsConstructor() {
        // When - Utworzenie obiektu za pomocą domyślnego konstruktora
        Offer offer = new Offer();

        // Then - Sprawdzanie wyników
        assertNull(offer.getId(), "Domyślne ID powinno być null.");
        assertNull(offer.getTitle(), "Domyślny tytuł powinien być null.");
        assertNull(offer.getDescription(), "Domyślny opis powinien być null.");
        assertNull(offer.getLocation(), "Domyślna lokalizacja powinna być null.");
        assertNull(offer.getSalaryRange(), "Domyślny zakres wynagrodzenia powinien być null.");
        assertNull(offer.getLevel(), "Domyślne technologie powinny być null.");
        assertFalse(offer.isApplied(), "Domyślna wartość applied powinna być false.");
        assertNull(offer.getFetchedAt(), "Domyślny czas fetchedAt powinien być null.");
    }
}
