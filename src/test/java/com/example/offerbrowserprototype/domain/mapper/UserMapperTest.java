package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.Offer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe dla klasy {@link OfferMapper}.
 * Klasa ta odpowiada za mapowanie obiektów {@link Offer} na {@link OfferDTO} i odwrotnie.
 */
class OfferMapperTest {

    private OfferMapper offerMapper;

    /**
     * Inicjalizacja obiektu {@link OfferMapper} przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        offerMapper = new OfferMapper();
    }

    /**
     * Test sprawdzający mapowanie obiektu {@link Offer} na {@link OfferDTO}.
     * Dane wejściowe ustawiane są w obiekcie {@link Offer}, a wynik mapowania porównywany jest z oczekiwanymi wartościami.
     */
    @Test
    void shouldMapOfferToOfferDTO() {
        // Given - Dane testowe
        Offer offer = new Offer();
        offer.setId("1");
        offer.setTitle("Java Developer");
        offer.setDescription("Exciting job opportunity for a Java Developer.");
        offer.setLocation("Warsaw");
        offer.setSalaryRange("10,000 - 15,000 PLN");
        offer.setTechnologies("Java, Spring Boot");
        offer.setApplied(true);
        offer.setFetchedAt(LocalDateTime.now());

        // When - Wywołanie metody
        OfferDTO dto = offerMapper.toDTO(offer);

        // Then - Sprawdzenie wyników
        assertEquals(offer.getId(), dto.getId(), "ID powinno być takie samo");
        assertEquals(offer.getTitle(), dto.getTitle(), "Tytuł powinien być taki sam");
        assertEquals(offer.getDescription(), dto.getDescription(), "Opis powinien być taki sam");
        assertEquals(offer.getLocation(), dto.getLocation(), "Lokalizacja powinna być taka sama");
        assertEquals(offer.getSalaryRange(), dto.getSalaryRange(), "Zakres wynagrodzenia powinien być taki sam");
        assertEquals(offer.getTechnologies(), dto.getTechnologies(), "Technologie powinny być takie same");
        assertEquals(offer.isApplied(), dto.isApplied(), "Status aplikacji powinien być taki sam");
        assertEquals(offer.getFetchedAt(), dto.getFetchedAt(), "Czas pobrania powinien być taki sam");
    }

    /**
     * Test sprawdzający mapowanie obiektu {@link OfferDTO} na {@link Offer}.
     * Dane wejściowe ustawiane są w obiekcie {@link OfferDTO}, a wynik mapowania porównywany jest z oczekiwanymi wartościami.
     */
    @Test
    void shouldMapOfferDTOToOffer() {
        // Given - Dane testowe
        OfferDTO dto = new OfferDTO();
        dto.setTitle("Java Developer");
        dto.setDescription("Exciting job opportunity for a Java Developer.");
        dto.setLocation("Warsaw");
        dto.setSalaryRange("10,000 - 15,000 PLN");
        dto.setTechnologies("Java, Spring Boot");
        dto.setApplied(true);
        dto.setFetchedAt(LocalDateTime.now());

        // When - Wywołanie metody
        Offer offer = offerMapper.toEntity(dto);

        // Then - Sprawdzenie wyników
        assertEquals(dto.getTitle(), offer.getTitle(), "Tytuł powinien być taki sam");
        assertEquals(dto.getDescription(), offer.getDescription(), "Opis powinien być taki sam");
        assertEquals(dto.getLocation(), offer.getLocation(), "Lokalizacja powinna być taka sama");
        assertEquals(dto.getSalaryRange(), offer.getSalaryRange(), "Zakres wynagrodzenia powinien być taki sam");
        assertEquals(dto.getTechnologies(), offer.getTechnologies(), "Technologie powinny być takie same");
        assertEquals(dto.isApplied(), offer.isApplied(), "Status aplikacji powinien być taki sam");
        assertEquals(dto.getFetchedAt(), offer.getFetchedAt(), "Czas pobrania powinien być taki sam");
    }
}
