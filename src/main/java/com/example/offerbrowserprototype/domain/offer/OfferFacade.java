package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfferFacade {

    private final OfferAdditionHandler additionHandler;
    private final OfferUpdateHandler updateHandler;
    private final OfferDeletionHandler deletionHandler;
    private final OfferRetrievalHandler retrievalHandler;
    private final OfferCacheService offerCacheService;

    public OfferFacade(OfferAdditionHandler additionHandler,
                       OfferUpdateHandler updateHandler,
                       OfferDeletionHandler deletionHandler,
                       OfferRetrievalHandler retrievalHandler,
                       OfferCacheService offerCacheService) {
        this.additionHandler = additionHandler;
        this.updateHandler = updateHandler;
        this.deletionHandler = deletionHandler;
        this.retrievalHandler = retrievalHandler;
        this.offerCacheService = offerCacheService;
    }

    public OfferDTO addOffer(OfferDTO offerDto) {
        return additionHandler.addOffer(offerDto);
    }

    public OfferDTO updateOffer(Long id, OfferDTO offerDto) {
        return updateHandler.updateOffer(id, offerDto);
    }

    public void deleteOffer(Long id) {
        deletionHandler.deleteOffer(id);
    }

    public OfferDTO getOffer(Long id) {
        return retrievalHandler.getOffer(id);
    }

    public List<OfferDTO> getAllOffers() {
        // Sprawdź czy oferty są w cache
        List<OfferDTO> cachedOffers = offerCacheService.getCachedOffers();
        if (cachedOffers != null && !cachedOffers.isEmpty()) {
            return cachedOffers;
        }

        // Jeśli nie ma ofert w cache, pobierz z retrievalHandler
        List<OfferDTO> offers = retrievalHandler.getAllOffers();

        // Zapisz oferty w cache
        offerCacheService.cacheOffers(offers);

        return offers;
    }
}
