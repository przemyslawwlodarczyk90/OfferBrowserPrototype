package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testy jednostkowe dla klasy {@link OfferAdditionHandler}.
 */
class OfferAdditionHandlerTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferMapper offerMapper;

    private Clock fixedClock;
    private OfferAdditionHandler offerAdditionHandler;

    /**
     * Inicjalizacja zasobów przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fixedClock = Clock.fixed(Instant.parse("2024-01-01T12:00:00Z"), ZoneId.of("UTC"));
        offerAdditionHandler = new OfferAdditionHandler(offerRepository, offerMapper, fixedClock);
    }

    /**
     * Test sprawdzający poprawne dodanie oferty.
     */
    @Test
    void shouldAddOfferSuccessfully() {
        // Given - Dane testowe
        OfferDTO offerDto = new OfferDTO();
        offerDto.setTitle("Java Developer");
        offerDto.setDescription("Exciting job opportunity.");
        offerDto.setLocation("Warsaw");
        offerDto.setSalaryRange("10,000 - 15,000 PLN");
        offerDto.setLevel("Java, Spring Boot");

        Offer offer = new Offer();
        offer.setTitle("Java Developer");
        offer.setDescription("Exciting job opportunity.");
        offer.setLocation("Warsaw");
        offer.setSalaryRange("10,000 - 15,000 PLN");
        offer.setLevel("Java, Spring Boot");
        offer.setFetchedAt(LocalDateTime.now(fixedClock));

        Offer savedOffer = new Offer();
        savedOffer.setId("1");
        savedOffer.setTitle("Java Developer");
        savedOffer.setDescription("Exciting job opportunity.");
        savedOffer.setLocation("Warsaw");
        savedOffer.setSalaryRange("10,000 - 15,000 PLN");
        savedOffer.setLevel("Java, Spring Boot");
        savedOffer.setFetchedAt(LocalDateTime.now(fixedClock));

        OfferDTO savedOfferDto = new OfferDTO();
        savedOfferDto.setId("1");
        savedOfferDto.setTitle("Java Developer");
        savedOfferDto.setDescription("Exciting job opportunity.");
        savedOfferDto.setLocation("Warsaw");
        savedOfferDto.setSalaryRange("10,000 - 15,000 PLN");
        savedOfferDto.setLevel("Java, Spring Boot");
        savedOfferDto.setFetchedAt(LocalDateTime.now(fixedClock));

        // Mockowanie metod mappera i repozytorium
        Mockito.when(offerMapper.toEntity(offerDto)).thenReturn(offer);
        Mockito.when(offerRepository.save(any())).thenReturn(savedOffer);
        Mockito.when(offerMapper.toDTO(savedOffer)).thenReturn(savedOfferDto);

        // When - Wywołanie metody
        OfferDTO result = offerAdditionHandler.addOffer(offerDto);

        // Then - Sprawdzenie wyników
        assertEquals("1", result.getId(), "ID powinno być zgodne z zapisanym obiektem.");
        assertEquals("Java Developer", result.getTitle(), "Tytuł powinien być zgodny z zapisanym obiektem.");
        assertEquals("Exciting job opportunity.", result.getDescription(), "Opis powinien być zgodny z zapisanym obiektem.");
        assertEquals("Warsaw", result.getLocation(), "Lokalizacja powinna być zgodna z zapisanym obiektem.");
        assertEquals("10,000 - 15,000 PLN", result.getSalaryRange(), "Zakres wynagrodzenia powinien być zgodny z zapisanym obiektem.");
        assertEquals("Java, Spring Boot", result.getLevel(), "Technologie powinny być zgodne z zapisanym obiektem.");
        assertEquals(LocalDateTime.now(fixedClock), result.getFetchedAt(), "Czas fetchedAt powinien być zgodny z czasem ustawionym przez Clock.");
    }
}
