package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.Offer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe dla klasy OfferMapper.
 * Klasa ta zajmuje się mapowaniem obiektów `Offer` na `OfferDTO` i odwrotnie.
 */
class OfferMapperTest {

    private OfferMapper offerMapper;

    /**
     * Przygotowanie środowiska testowego przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        offerMapper = new OfferMapper();
    }

    /**
     * Test mapowania obiektu `Offer` na `OfferDTO`.
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

        // When - Wywołanie metody mapującej
        OfferDTO dto = offerMapper.toDTO(offer);

        // Then - Weryfikacja wyników
        assertEquals(offer.getId(), dto.getId());
        assertEquals(offer.getTitle(), dto.getTitle());
        assertEquals(offer.getDescription(), dto.getDescription());
        assertEquals(offer.getLocation(), dto.getLocation());
        assertEquals(offer.getSalaryRange(), dto.getSalaryRange());
        assertEquals(offer.getTechnologies(), dto.getTechnologies());
        assertEquals(offer.isApplied(), dto.isApplied());
        assertEquals(offer.getFetchedAt(), dto.getFetchedAt());
    }

    /**
     * Test mapowania obiektu `OfferDTO` na `Offer`.
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

        // When - Wywołanie metody mapującej
        Offer offer = offerMapper.toEntity(dto);

        // Then - Weryfikacja wyników
        assertEquals(dto.getTitle(), offer.getTitle());
        assertEquals(dto.getDescription(), offer.getDescription());
        assertEquals(dto.getLocation(), offer.getLocation());
        assertEquals(dto.getSalaryRange(), offer.getSalaryRange());
        assertEquals(dto.getTechnologies(), offer.getTechnologies());
        assertEquals(dto.isApplied(), offer.isApplied());
        assertEquals(dto.getFetchedAt(), offer.getFetchedAt());
    }

    /**
     * Test mapowania `Offer` na `OfferDTO` z pustymi polami.
     */
    @Test
    void shouldHandleNullFieldsWhenMappingOfferToDTO() {
        // Given - Obiekt `Offer` z pustymi polami
        Offer offer = new Offer();

        // When - Wywołanie metody mapującej
        OfferDTO dto = offerMapper.toDTO(offer);

        // Then - Weryfikacja wyników
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
        assertNull(dto.getLocation());
        assertNull(dto.getSalaryRange());
        assertNull(dto.getTechnologies());
        assertFalse(dto.isApplied());
        assertNull(dto.getFetchedAt());
    }

    /**
     * Test mapowania `OfferDTO` na `Offer` z pustymi polami.
     */
    @Test
    void shouldHandleNullFieldsWhenMappingDTOToOffer() {
        // Given - Obiekt `OfferDTO` z pustymi polami
        OfferDTO dto = new OfferDTO();

        // When - Wywołanie metody mapującej
        Offer offer = offerMapper.toEntity(dto);

        // Then - Weryfikacja wyników
        assertNull(offer.getId());
        assertNull(offer.getTitle());
        assertNull(offer.getDescription());
        assertNull(offer.getLocation());
        assertNull(offer.getSalaryRange());
        assertNull(offer.getTechnologies());
        assertFalse(offer.isApplied());
        assertNull(offer.getFetchedAt());
    }
}
