package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.*;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheFacade;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfferFacade {

    private final OfferFromUrlHandler    offerFromUrlHandler;
    private final OfferDetailsHandler    detailsHandler;
    private final OfferAdditionHandler   additionHandler;
    private final OfferUpdateHandler     updateHandler;
    private final OfferDeletionHandler   deletionHandler;
    private final OfferRetrievalHandler  retrievalHandler;
    private final OfferCacheFacade       offerCacheFacade;
    private final MarkAsDuplicateHandler markAsDuplicateHandler;

    public OfferFacade(
            OfferFromUrlHandler    offerFromUrlHandler,
            OfferAdditionHandler   additionHandler,
            OfferUpdateHandler     updateHandler,
            OfferDeletionHandler   deletionHandler,
            OfferRetrievalHandler  retrievalHandler,
            OfferDetailsHandler    detailsHandler,
            OfferCacheFacade       offerCacheFacade,
            MarkAsDuplicateHandler markAsDuplicateHandler
    ) {
        this.offerFromUrlHandler    = offerFromUrlHandler;
        this.additionHandler        = additionHandler;
        this.updateHandler          = updateHandler;
        this.deletionHandler        = deletionHandler;
        this.retrievalHandler       = retrievalHandler;
        this.detailsHandler         = detailsHandler;
        this.offerCacheFacade       = offerCacheFacade;
        this.markAsDuplicateHandler = markAsDuplicateHandler;
    }

    @CacheEvict(value = "offers", allEntries = true)
    public OfferDTO addOffer(OfferDTO dto) {
        return additionHandler.addOffer(dto);
    }

    @CacheEvict(value = "offers", allEntries = true)
    public OfferDTO updateOffer(Long id, OfferDTO dto) {
        return updateHandler.updateOffer(id, dto);
    }

    public OfferDTO getOffer(Long id) {
        return detailsHandler.getOfferById(id);
    }

    @CacheEvict(value = "offers", allEntries = true)
    public void deleteOffer(Long id) {
        deletionHandler.deleteOffer(id);
    }

    public List<OfferDTO> getAllOffers() {
        try {
            List<OfferDTO> cached = offerCacheFacade.getCachedOffers();
            if (cached != null && !cached.isEmpty()) {
                return cached;
            }
        } catch (Exception ignored) {}

        List<OfferDTO> offers = retrievalHandler.getAllOffers();

        if (!offers.isEmpty()) {
            try {
                offerCacheFacade.cacheOffers(offers);
            } catch (Exception ignored) {}
        }

        return offers;
    }

    public OfferDTO addOfferFromUrl(Long userId, String offerUrl) {
        return offerFromUrlHandler.addOfferFromUrl(userId, offerUrl);
    }

    @CacheEvict(value = "offers", allEntries = true)
    public void markAsDuplicateById(Long userId, Long offerId) {
        markAsDuplicateHandler.handleById(userId, offerId);
    }

    @CacheEvict(value = "offers", allEntries = true)
    public void markAsDuplicateByUrl(Long userId, String offerUrl) {
        markAsDuplicateHandler.handleByUrl(userId, offerUrl);
    }
}
