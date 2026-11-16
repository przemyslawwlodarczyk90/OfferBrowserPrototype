package com.example.offerbrowserprototype.domain.offer;

import java.util.Set;

/**
 * Minimum an offer must have before it is stored: a URL of its portal, a title and
 * a company. The scrapers' fallback texts ("Brak tytułu", ...) do not count — they
 * mean the page could not be parsed (e.g. an error page was returned).
 */
public final class OfferValidation {

    static final Set<String> PLACEHOLDER_TITLES = Set.of("brak tytułu");
    static final Set<String> PLACEHOLDER_COMPANIES = Set.of("brak informacji o firmie");

    private OfferValidation() {
    }

    /** @return reason the offer must not be stored, or {@code null} when it is valid */
    public static String rejectionReason(String offerUrl, String title, String company, OfferSource source) {
        if (offerUrl == null || offerUrl.isBlank()) {
            return "brak offerUrl";
        }
        if (source != null && !source.matchesUrl(offerUrl)) {
            return "adres spoza domeny " + source.displayName();
        }
        if (isMissing(title, PLACEHOLDER_TITLES)) {
            return "brak tytułu oferty";
        }
        if (isMissing(company, PLACEHOLDER_COMPANIES)) {
            return "brak nazwy firmy";
        }
        return null;
    }

    private static boolean isMissing(String value, Set<String> placeholders) {
        return value == null || value.isBlank() || placeholders.contains(value.trim().toLowerCase());
    }
}
