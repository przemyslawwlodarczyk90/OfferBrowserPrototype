package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.Offer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class OfferMapperTest {

    private OfferMapper offerMapper;


    @BeforeEach
    void setUp() {
        offerMapper = new OfferMapper();
    }


    @Test
    void shouldMapOfferToOfferDTO() {
        // Given - Dane testowe
        Offer offer = new Offer();
        offer.setId("1");
        offer.setTitle("Java Developer");
        offer.setDescription("Exciting job opportunity for a Java Developer.");
        offer.setLocation("Warsaw");
        offer.setSalaryRange("10,000 - 15,000 PLN");
        offer.setLevel("Java, Spring Boot");

        offer.setFetchedAt(LocalDateTime.now());

        OfferDTO dto = offerMapper.toDTO(offer);

        assertEquals(offer.getId(), dto.getId());
        assertEquals(offer.getTitle(), dto.getTitle());
        assertEquals(offer.getDescription(), dto.getDescription());
        assertEquals(offer.getLocation(), dto.getLocation());
        assertEquals(offer.getSalaryRange(), dto.getSalaryRange());
        assertEquals(offer.getLevel(), dto.getLevel());

        assertEquals(offer.getFetchedAt(), dto.getFetchedAt());
    }

    @Test
    void shouldMapOfferDTOToOffer() {
        // Given
        OfferDTO dto = new OfferDTO();
        dto.setTitle("Java Developer");
        dto.setDescription("Exciting job opportunity for a Java Developer.");
        dto.setLocation("Warsaw");
        dto.setSalaryRange("10,000 - 15,000 PLN");
        dto.setLevel("Java, Spring Boot");

        dto.setFetchedAt(LocalDateTime.now());

        // When
        Offer offer = offerMapper.toEntity(dto);

        // Then
        assertEquals(dto.getTitle(), offer.getTitle());
        assertEquals(dto.getDescription(), offer.getDescription());
        assertEquals(dto.getLocation(), offer.getLocation());
        assertEquals(dto.getSalaryRange(), offer.getSalaryRange());
        assertEquals(dto.getLevel(), offer.getLevel());

        assertEquals(dto.getFetchedAt(), offer.getFetchedAt());
    }

    @Test
    void shouldHandleNullFieldsWhenMappingOfferToDTO() {
        // Given
        Offer offer = new Offer();

        // When
        OfferDTO dto = offerMapper.toDTO(offer);

        // Then
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
        assertNull(dto.getLocation());
        assertNull(dto.getSalaryRange());
        assertNull(dto.getLevel());

        assertNull(dto.getFetchedAt());
    }


    @Test
    void shouldHandleNullFieldsWhenMappingDTOToOffer() {
        // Given
        OfferDTO dto = new OfferDTO();

        // When
        Offer offer = offerMapper.toEntity(dto);

        // Then
        assertNull(offer.getId());
        assertNull(offer.getTitle());
        assertNull(offer.getDescription());
        assertNull(offer.getLocation());
        assertNull(offer.getSalaryRange());
        assertNull(offer.getLevel());

        assertNull(offer.getFetchedAt());
    }
}
