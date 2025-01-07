package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OfferFromUrlHandlerTest {

    private OfferRepository offerRepository;
    private OfferMapper offerMapper;
    private ApplicationNoteHandler applicationNoteHandler;
    private OfferFromUrlHandler offerFromUrlHandler;

    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        offerMapper = mock(OfferMapper.class);
        applicationNoteHandler = mock(ApplicationNoteHandler.class);

        offerFromUrlHandler = new OfferFromUrlHandler(offerRepository, offerMapper, applicationNoteHandler);
        offerFromUrlHandler.pythonPath = "python"; // Symulacja poprawnej ścieżki do Pythona
        offerFromUrlHandler.scriptPath = "/path/to/fake/script.py"; // Symulacja ścieżki do skryptu
    }

    @Test
    void shouldThrowExceptionForEmptyUrl() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            offerFromUrlHandler.addOfferFromUrl("");
        });
        assertThat(exception.getMessage()).isEqualTo("Offer URL cannot be null or empty");
    }

    @Test
    void shouldThrowExceptionIfOfferAlreadyExists() {
        // Przygotowanie danych testowych
        String offerUrl = "https://example.com/offer/123";
        when(offerRepository.findByOfferUrl(offerUrl)).thenReturn(Optional.of(new Offer()));

        // Weryfikacja
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            offerFromUrlHandler.addOfferFromUrl(offerUrl);
        });
        assertThat(exception.getMessage()).isEqualTo("Offer already exists in the database");
    }
    @Test
    void shouldHandleNewOfferSuccessfully() throws Exception {
        // Przygotowanie danych testowych
        String offerUrl = "https://example.com/offer/123";
        OfferDTO mockOfferDTO = new OfferDTO();
        mockOfferDTO.setOfferUrl(offerUrl);
        mockOfferDTO.setCompany("Test Company");

        Offer mockOffer = new Offer();
        mockOffer.setId("1");
        mockOffer.setOfferUrl(offerUrl);
        mockOffer.setCompany("Test Company");

        // Symulacja braku istniejącej oferty w bazie
        when(offerRepository.findByOfferUrl(offerUrl)).thenReturn(Optional.empty());
        when(offerMapper.toEntity(any())).thenReturn(mockOffer);
        when(offerRepository.save(any(Offer.class))).thenAnswer(invocation -> {
            Offer offer = invocation.getArgument(0);
            offer.setId("1"); // Symulacja przypisania ID podczas zapisu
            return offer;
        });
        when(offerMapper.toDTO(any(Offer.class))).thenReturn(mockOfferDTO);

        // Symulacja działania metody handleOfferFromUrl
        OfferFromUrlHandler spyHandler = spy(offerFromUrlHandler);
        doReturn(mockOfferDTO).when(spyHandler).handleOfferFromUrl(offerUrl);

        // Wywołanie metody
        OfferDTO result = spyHandler.addOfferFromUrl(offerUrl);

        // Weryfikacja
        verify(offerRepository, times(1)).save(any(Offer.class));
        verify(applicationNoteHandler, times(1)).saveApplicationNote("1", offerUrl, "Test Company");
        assertThat(result).isNotNull();
        assertThat(result.getOfferUrl()).isEqualTo(offerUrl);
    }


}
