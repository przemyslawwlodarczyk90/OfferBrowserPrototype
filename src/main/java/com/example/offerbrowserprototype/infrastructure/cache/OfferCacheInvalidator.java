package com.example.offerbrowserprototype.infrastructure.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * Drops every cached offer list and statistic after data changes (imports,
 * deletions). Works with or without Redis being enabled.
 */
@Component
public class OfferCacheInvalidator {

    private static final Logger logger = LoggerFactory.getLogger(OfferCacheInvalidator.class);

    private final ObjectProvider<OfferCacheFacade> offerCache;
    private final ObjectProvider<CacheManager> cacheManager;

    public OfferCacheInvalidator(ObjectProvider<OfferCacheFacade> offerCache,
                                 ObjectProvider<CacheManager> cacheManager) {
        this.offerCache = offerCache;
        this.cacheManager = cacheManager;
    }

    public void invalidateAll() {
        try {
            offerCache.ifAvailable(OfferCacheFacade::clearCache);
            cacheManager.ifAvailable(cm -> {
                for (String name : CacheKeys.STATISTICS_CACHES) {
                    Cache cache = cm.getCache(name);
                    if (cache != null) cache.clear();
                }
                Cache offers = cm.getCache("offers");
                if (offers != null) offers.clear();
            });
        } catch (RuntimeException e) {
            logger.warn("Could not invalidate offer caches: {}", e.getMessage());
        }
    }
}
