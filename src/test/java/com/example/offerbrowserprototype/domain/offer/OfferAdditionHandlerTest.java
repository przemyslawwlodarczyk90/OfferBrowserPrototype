package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OfferAdditionHandlerTest {

    private OfferRepository offerRepository;
    private OfferMapper offerMapper;
    private Clock clock;
    private OfferAdditionHandler offerAdditionHandler;

    @BeforeEach
    void setUp() {
        offerRepository = Mockito.mock(OfferRepository.class);
        offerMapper = Mockito.mock(OfferMapper.class);
        clock = Clock.fixed(ZonedDateTime.of(2025, 1, 7, 12, 0, 0, 0, ZoneId.of("UTC")).toInstant(), ZoneId.of("UTC"));
        offerAdditionHandler = new OfferAdditionHandler(offerRepository, offerMapper, clock);
    }

    @Test
    void shouldAddOfferSuccessfully() {
        // Given
        OfferDTO inputDto = new OfferDTO("Test Title", "Test Description", "Test Location",
                "https://example.com/offer", "5000-7000", "Test Company", "Mid", false, null);

        Offer mappedOffer = new Offer();
        mappedOffer.setTitle("Test Title");
        mappedOffer.setDescription("Test Description");
        mappedOffer.setLocation("Test Location");
        mappedOffer.setOfferUrl("https://example.com/offer");
        mappedOffer.setSalaryRange("5000-7000");
        mappedOffer.setCompany("Test Company");
        mappedOffer.setLevel("Mid");

        Offer savedOffer = new Offer();
        savedOffer.setId("123");
        savedOffer.setTitle("Test Title");
        savedOffer.setDescription("Test Description");
        savedOffer.setLocation("Test Location");
        savedOffer.setOfferUrl("https://example.com/offer");
        savedOffer.setSalaryRange("5000-7000");
        savedOffer.setCompany("Test Company");
        savedOffer.setLevel("Mid");
        savedOffer.setFetchedAt(LocalDateTime.now(clock));

        OfferDTO expectedDto = new OfferDTO("123", "Test Title", "Test Description", "Test Location",
                "https://example.com/offer", "5000-7000", "Test Company", "Mid", false, LocalDateTime.now(clock));

        when(offerMapper.toEntity(inputDto)).thenReturn(mappedOffer);
        when(offerRepository.save(any(Offer.class))).thenReturn(savedOffer);
        when(offerMapper.toDTO(savedOffer)).thenReturn(expectedDto);

        // When
        OfferDTO result = offerAdditionHandler.addOffer(inputDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("123");
        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getFetchedAt()).isEqualTo(LocalDateTime.now(clock));

        verify(offerMapper).toEntity(inputDto);
        verify(offerRepository).save(mappedOffer);
        verify(offerMapper).toDTO(savedOffer);
    }

    @Test
    void shouldCallRepositorySaveMethod() {
        // Given
        OfferDTO inputDto = new OfferDTO("Title", "Description", "Location",
                "https://example.com/offer", "4000-6000", "Company", "Junior", false, null);
        Offer mappedOffer = new Offer();
        when(offerMapper.toEntity(inputDto)).thenReturn(mappedOffer);

        Offer savedOffer = new Offer();
        when(offerRepository.save(any(Offer.class))).thenReturn(savedOffer);
        when(offerMapper.toDTO(savedOffer)).thenReturn(new OfferDTO());

        // When
        offerAdditionHandler.addOffer(inputDto);

        // Then
        verify(offerRepository).save(mappedOffer);
    }

    @Test
    void shouldSetFetchedAtToCurrentTime() {
        // Given
        OfferDTO inputDto = new OfferDTO("Title", "Description", "Location",
                "https://example.com/offer", "4000-6000", "Company", "Junior", false, null);
        Offer mappedOffer = new Offer();
        when(offerMapper.toEntity(inputDto)).thenReturn(mappedOffer);

        Offer savedOffer = new Offer();
        when(offerRepository.save(any(Offer.class))).thenReturn(savedOffer);

        // When
        offerAdditionHandler.addOffer(inputDto);

        // Then
        assertThat(mappedOffer.getFetchedAt()).isEqualTo(LocalDateTime.now(clock));
    }
}
