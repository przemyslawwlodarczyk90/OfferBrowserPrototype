package com.example.offerbrowserprototype.infrastructure.cache;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(value = "spring.cache.type", havingValue = "redis")
public class OfferCacheFacade {

    private final OfferCacheRetrievalService retrievalService;
    private final OfferCacheStorageService storageService;
    private final OfferCacheDeletionService deletionService;

    public OfferCacheFacade(OfferCacheRetrievalService retrievalService,
                            OfferCacheStorageService storageService,
                            OfferCacheDeletionService deletionService) {
        this.retrievalService = retrievalService;
        this.storageService = storageService;
        this.deletionService = deletionService;
    }

    /** @param source portal, or {@code null} for all portals */
    public List<OfferDTO> getCachedOffers(OfferSource source) {
        return retrievalService.getCachedOffers(source);
    }

    public void cacheOffers(OfferSource source, List<OfferDTO> offers) {
        storageService.cacheOffers(source, offers);
    }

    public void clearCache() {
        deletionService.clearCache();
    }
}
