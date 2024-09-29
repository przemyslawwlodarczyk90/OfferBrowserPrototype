package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheFacade;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfferFacade {

    private final OfferAdditionHandler additionHandler;
    private final OfferUpdateHandler updateHandler;
    private final OfferDeletionHandler deletionHandler;
    private final OfferRetrievalHandler retrievalHandler;
    private final OfferCacheFacade offerCacheFacade; // Zmiana z OfferCacheService na OfferCacheFacade

    public OfferFacade(OfferAdditionHandler additionHandler,
                       OfferUpdateHandler updateHandler,
                       OfferDeletionHandler deletionHandler,
                       OfferRetrievalHandler retrievalHandler,
                       OfferCacheFacade offerCacheFacade) { // Zmiana z OfferCacheService na OfferCacheFacade
        this.additionHandler = additionHandler;
        this.updateHandler = updateHandler;
        this.deletionHandler = deletionHandler;
        this.retrievalHandler = retrievalHandler;
        this.offerCacheFacade = offerCacheFacade; // Zmiana z OfferCacheService na OfferCacheFacade
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



    public List<OfferDTO> getAllOffers() {
        // Sprawdź czy oferty są w cache
        List<OfferDTO> cachedOffers = offerCacheFacade.getCachedOffers(); // Użyj offerCacheFacade
        if (cachedOffers != null && !cachedOffers.isEmpty()) {
            return cachedOffers;
        }

        // Jeśli nie ma ofert w cache, pobierz z retrievalHandler
        List<OfferDTO> offers = retrievalHandler.getAllOffers();

        // Zapisz oferty w cache
        offerCacheFacade.cacheOffers(offers); // Użyj offerCacheFacade

        return offers;
    }
}
