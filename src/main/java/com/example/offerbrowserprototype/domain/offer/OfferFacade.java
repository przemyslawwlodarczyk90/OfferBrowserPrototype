package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheFacade;
import com.example.offerbrowserprototype.infrastructure.service.ExternalJobOfferService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class OfferFacade {

    private final OfferAdditionHandler additionHandler;
    private final OfferUpdateHandler updateHandler;
    private final OfferDeletionHandler deletionHandler;
    private final OfferRetrievalHandler retrievalHandler;
    private final OfferCacheFacade offerCacheFacade;
    private final ExternalJobOfferService externalJobOfferService;

    public OfferFacade(OfferAdditionHandler additionHandler,
                       OfferUpdateHandler updateHandler,
                       OfferDeletionHandler deletionHandler,
                       OfferRetrievalHandler retrievalHandler,
                       OfferCacheFacade offerCacheFacade,
                       ExternalJobOfferService externalJobOfferService) {
        this.additionHandler = additionHandler;
        this.updateHandler = updateHandler;
        this.deletionHandler = deletionHandler;
        this.retrievalHandler = retrievalHandler;
        this.offerCacheFacade = offerCacheFacade;
        this.externalJobOfferService = externalJobOfferService;
    }

    public OfferDTO addOffer(OfferDTO offerDto) {
        return additionHandler.addOffer(offerDto);
    }

    public OfferDTO updateOffer(String id, OfferDTO offerDto) {
        return updateHandler.updateOffer(id, offerDto);
    }

    public OfferDTO getOffer(String id) {
        return retrievalHandler.getOffer(id);
    }

    public void deleteOffer(String id) {
        deletionHandler.deleteOffer(id);
    }

    // Główna metoda do pobierania wszystkich ofert (łączy oferty lokalne i zewnętrzne)
    public List<OfferDTO> getAllOffers() {
        // Najpierw spróbuj pobrać dane z cache'a
        List<OfferDTO> cachedOffers = offerCacheFacade.getCachedOffers();
        if (cachedOffers != null && !cachedOffers.isEmpty()) {
            return cachedOffers;
        }

        // Jeśli cache jest pusty, pobierz dane z bazy danych
        List<OfferDTO> localOffers = retrievalHandler.getAllOffers();

        // Pobierz oferty z zewnętrznych źródeł
        List<OfferDTO> externalOffers = externalJobOfferService.fetchExternalOffers();

        // Złącz listy ofert lokalnych i zewnętrznych
        List<OfferDTO> combinedOffers = new ArrayList<>(localOffers);
        combinedOffers.addAll(externalOffers);

        // Zapisz złączone oferty w cache'u na przyszłe zapytania
        offerCacheFacade.cacheOffers(combinedOffers);

        return combinedOffers;
    }
}
