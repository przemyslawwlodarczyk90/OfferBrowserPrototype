package com.example.offerbrowserprototype.infrastructure.cache;

import com.example.offerbrowserprototype.domain.offer.OfferSource;

import java.util.ArrayList;
import java.util.List;

public class CacheKeys {
    public static final String JOB_OFFERS = "job_offers";

    /** Spring caches holding per-source statistics; evicted after every import. */
    public static final List<String> STATISTICS_CACHES = List.of("totalOffers", "levelDistribution", "cityDistribution",
            "levelDistributionBySource");

    /** Offer list cache key — one entry per portal plus one for "all portals". */
    public static String offers(OfferSource source) {
        return JOB_OFFERS + ":" + (source == null ? "ALL" : source.value());
    }

    public static List<String> allOfferKeys() {
        List<String> keys = new ArrayList<>();
        keys.add(JOB_OFFERS);          // legacy single-key cache
        keys.add(offers(null));
        for (OfferSource s : OfferSource.values()) {
            keys.add(offers(s));
        }
        return keys;
    }
}
