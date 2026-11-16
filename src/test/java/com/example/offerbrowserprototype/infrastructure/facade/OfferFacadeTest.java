package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.domain.offer.*;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

// Testy jednostkowe fasady ofert — weryfikują delegację do odpowiednich handlerów
@ExtendWith(MockitoExtension.class)
class OfferFacadeTest {

    @Mock private OfferFromUrlHandler offerFromUrlHandler;
    @Mock private OfferDetailsHandler detailsHandler;
    @Mock private OfferAdditionHandler additionHandler;
    @Mock private OfferUpdateHandler updateHandler;
    @Mock private OfferDeletionHandler deletionHandler;
    @Mock private OfferRetrievalHandler retrievalHandler;
    @Mock private OfferCacheFacade offerCacheFacade;
    @Mock private MarkAsDuplicateHandler markAsDuplicateHandler;
    @InjectMocks private OfferFacade facade;

    @Test
    void addOffer_deleguje_do_handlera_dodawania() {
        // given
        OfferDTO dto = dto("T");
        when(additionHandler.addOffer(dto)).thenReturn(dto);

        // when / then
        assertThat(facade.addOffer(dto)).isEqualTo(dto);
        verify(additionHandler).addOffer(dto);
    }

    @Test
    void deleteOffer_deleguje_do_handlera_usuwania() {
        // when
        facade.deleteOffer(5L);

        // then
        verify(deletionHandler).deleteOffer(5L);
    }

    @Test
    void updateOffer_deleguje_do_handlera_aktualizacji() {
        // given
        OfferDTO dto = dto("Nowy");
        when(updateHandler.updateOffer(1L, dto)).thenReturn(dto);

        // when / then
        assertThat(facade.updateOffer(1L, dto)).isEqualTo(dto);
        verify(updateHandler).updateOffer(1L, dto);
    }

    @Test
    void getOffer_deleguje_do_handlera_szczegulow() {
        // given
        OfferDTO dto = dto("T");
        when(detailsHandler.getOfferById(1L)).thenReturn(dto);

        // when / then
        assertThat(facade.getOffer(1L)).isEqualTo(dto);
    }

    @Test
    void getAllOffers_zwraca_z_cache_gdy_dostepne() {
        // given — cache zawiera wyniki, handler pobierania nie powinien być wywołany
        List<OfferDTO> cached = List.of(dto("cached"));
        when(offerCacheFacade.getCachedOffers(null)).thenReturn(cached);

        // when
        List<OfferDTO> result = facade.getAllOffers();

        // then
        assertThat(result).isEqualTo(cached);
        verifyNoInteractions(retrievalHandler);
    }

    @Test
    void getAllOffers_pobiera_z_bazy_gdy_cache_pusty() {
        // given — brak cache, fallback do bazy
        when(offerCacheFacade.getCachedOffers(null)).thenReturn(List.of());
        List<OfferDTO> fromDb = List.of(dto("z bazy"));
        when(retrievalHandler.getAllOffers()).thenReturn(fromDb);

        // when
        List<OfferDTO> result = facade.getAllOffers();

        // then
        assertThat(result).isEqualTo(fromDb);
        verify(retrievalHandler).getAllOffers();
    }

    @Test
    void getAllOffers_dla_portalu_uzywa_wlasnego_klucza_cache_i_filtra() {
        // given — cache theprotocol pusty; nie wolno zwrócić danych innego portalu
        when(offerCacheFacade.getCachedOffers(OfferSource.THEPROTOCOL)).thenReturn(List.of());
        List<OfferDTO> fromDb = List.of(dto("theprotocol"));
        when(retrievalHandler.getAllOffers(OfferSource.THEPROTOCOL)).thenReturn(fromDb);

        // when
        List<OfferDTO> result = facade.getAllOffers(OfferSource.THEPROTOCOL);

        // then
        assertThat(result).isEqualTo(fromDb);
        verify(offerCacheFacade).cacheOffers(OfferSource.THEPROTOCOL, fromDb);
        verify(offerCacheFacade, never()).getCachedOffers(null);
        verify(offerCacheFacade, never()).getCachedOffers(OfferSource.NOFLUFF);
    }

    @Test
    void deleteOffer_czysci_cache_list_ofert() {
        facade.deleteOffer(5L);
        verify(offerCacheFacade).clearCache();
    }

    private OfferDTO dto(String title) {
        return new OfferDTO(title, "D", "L", "http://u", "S", "C", "J", false, null);
    }
}
