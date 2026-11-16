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
        OfferDTO result = additionHandler.addOffer(dto);
        clearOfferListCache();
        return result;
    }

    @CacheEvict(value = "offers", allEntries = true)
    public OfferDTO updateOffer(Long id, OfferDTO dto) {
        OfferDTO result = updateHandler.updateOffer(id, dto);
        clearOfferListCache();
        return result;
    }

    public OfferDTO getOffer(Long id) {
        return detailsHandler.getOfferById(id);
    }

    @CacheEvict(value = "offers", allEntries = true)
    public void deleteOffer(Long id) {
        deletionHandler.deleteOffer(id);
        clearOfferListCache();
    }

    public List<OfferDTO> getAllOffers() {
        return getAllOffers(null);
    }

    /** @param source portal to filter by, or {@code null} for all portals; cached per portal */
    public List<OfferDTO> getAllOffers(OfferSource source) {
        try {
            List<OfferDTO> cached = offerCacheFacade.getCachedOffers(source);
            if (cached != null && !cached.isEmpty()) {
                return cached;
            }
        } catch (Exception ignored) {}

        List<OfferDTO> offers = source == null
                ? retrievalHandler.getAllOffers()
                : retrievalHandler.getAllOffers(source);

        if (!offers.isEmpty()) {
            try {
                offerCacheFacade.cacheOffers(source, offers);
            } catch (Exception ignored) {}
        }

        return offers;
    }

    private void clearOfferListCache() {
        try {
            offerCacheFacade.clearCache();
        } catch (Exception ignored) {}
    }

    public OfferDTO addOfferFromUrl(Long userId, String offerUrl) {
        OfferDTO result = offerFromUrlHandler.addOfferFromUrl(userId, offerUrl);
        clearOfferListCache();
        return result;
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
