package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OfferUpdateHandlerTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferMapper offerMapper;

    @Mock
    private Clock clock;

    private OfferUpdateHandler offerUpdateHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        offerUpdateHandler = new OfferUpdateHandler(offerRepository, offerMapper, clock);
    }

    /**
     * Test sprawdzający, czy oferta jest poprawnie aktualizowana.
     */
    @Test
    void shouldUpdateOfferSuccessfully() {
        // Given - dane testowe
        String offerId = "1";

        Offer existingOffer = new Offer();
        existingOffer.setId(offerId);
        existingOffer.setTitle("Java Developer");

        OfferDTO updateData = new OfferDTO();
        updateData.setTitle("Senior Java Developer");
        updateData.setDescription("Exciting opportunity for experienced Java Developer.");
        updateData.setLocation("Remote");
        updateData.setSalaryRange("15,000 - 20,000 PLN");
        updateData.setTechnologies("Java, Spring Boot, AWS");

        Offer updatedOffer = new Offer();
        updatedOffer.setId(offerId);
        updatedOffer.setTitle(updateData.getTitle());
        updatedOffer.setDescription(updateData.getDescription());
        updatedOffer.setLocation(updateData.getLocation());
        updatedOffer.setSalaryRange(updateData.getSalaryRange());
        updatedOffer.setTechnologies(updateData.getTechnologies());
        updatedOffer.setFetchedAt(LocalDateTime.now());

        OfferDTO updatedOfferDTO = new OfferDTO();
        updatedOfferDTO.setId(updatedOffer.getId());
        updatedOfferDTO.setTitle(updatedOffer.getTitle());
        updatedOfferDTO.setDescription(updatedOffer.getDescription());
        updatedOfferDTO.setLocation(updatedOffer.getLocation());
        updatedOfferDTO.setSalaryRange(updatedOffer.getSalaryRange());
        updatedOfferDTO.setTechnologies(updatedOffer.getTechnologies());

        when(offerRepository.findById(offerId)).thenReturn(Optional.of(existingOffer));
        when(clock.instant()).thenReturn(LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC));
        when(clock.getZone()).thenReturn(java.time.ZoneId.of("UTC"));
        when(offerRepository.save(any())).thenReturn(updatedOffer);
        when(offerMapper.toDTO(updatedOffer)).thenReturn(updatedOfferDTO);

        // When - wywołanie metody
        OfferDTO result = offerUpdateHandler.updateOffer(offerId, updateData);

        // Then - sprawdzenie wyników
        assertEquals(updatedOfferDTO, result);
        verify(offerRepository, times(1)).findById(offerId);
        verify(offerRepository, times(1)).save(existingOffer);
        verify(offerMapper, times(1)).toDTO(updatedOffer);
    }

    /**
     * Test sprawdzający, czy wyjątek jest rzucany, gdy oferta nie istnieje.
     */
    @Test
    void shouldThrowExceptionWhenOfferNotFound() {
        // Given - dane testowe
        String offerId = "1";
        OfferDTO updateData = new OfferDTO();
        updateData.setTitle("Senior Java Developer");

        when(offerRepository.findById(offerId)).thenReturn(Optional.empty());

        // When - wywołanie metody
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            offerUpdateHandler.updateOffer(offerId, updateData);
        });

        // Then - sprawdzenie wyników
        assertEquals("Offer not found", exception.getMessage());
        verify(offerRepository, times(1)).findById(offerId);
        verifyNoMoreInteractions(offerRepository);
        verifyNoInteractions(offerMapper);
    }
}
