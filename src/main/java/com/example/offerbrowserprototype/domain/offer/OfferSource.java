package com.example.offerbrowserprototype.domain.offer;

import java.net.URI;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * Job portals the application imports offers from. {@link #value()} is what is
 * stored in {@code job_offers.source} and used in the API ({@code ?source=}).
 */
public enum OfferSource {

    NOFLUFF("NoFluff", "No Fluff Jobs", Set.of("nofluffjobs.com")),
    THEPROTOCOL("TheProtocol", "theprotocol.it", Set.of("theprotocol.it")),
    JUSTJOIN("JustJoin", "Just Join IT", Set.of("justjoin.it"));

    private final String value;
    private final String displayName;
    private final Set<String> domains;

    OfferSource(String value, String displayName, Set<String> domains) {
        this.value = value;
        this.displayName = displayName;
        this.domains = domains;
    }

    public String value() {
        return value;
    }

    public String displayName() {
        return displayName;
    }

    /** Main domain of the portal, e.g. {@code justjoin.it}. */
    public String domain() {
        return domains.iterator().next();
    }

    /** Domains of all portals, for error messages ("nofluffjobs.com, theprotocol.it, justjoin.it"). */
    public static String supportedDomains() {
        return String.join(", ", Arrays.stream(values()).map(OfferSource::domain).toList());
    }

    /** Values of all portals, for error messages ("NoFluff, TheProtocol, JustJoin"). */
    public static String supportedValues() {
        return String.join(", ", Arrays.stream(values()).map(OfferSource::value).toList());
    }

    /** Parses an API parameter; blank means "all sources". */
    public static Optional<OfferSource> fromParam(String param) {
        if (param == null || param.isBlank()) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(s -> s.value.equalsIgnoreCase(param.trim()) || s.name().equalsIgnoreCase(param.trim()))
                .findFirst()
                .or(() -> {
                    throw new IllegalArgumentException("Nieznane źródło ofert: " + param);
                });
    }

    /** Resolves the portal from an offer URL's host; empty for unknown hosts. */
    public static Optional<OfferSource> fromUrl(String url) {
        String host;
        try {
            URI uri = URI.create(url.trim());
            if (!"https".equalsIgnoreCase(uri.getScheme()) && !"http".equalsIgnoreCase(uri.getScheme())) {
                return Optional.empty();
            }
            host = uri.getHost();
        } catch (RuntimeException e) {
            return Optional.empty();
        }
        if (host == null) {
            return Optional.empty();
        }
        String h = host.toLowerCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(s -> s.domains.stream().anyMatch(d -> h.equals(d) || h.endsWith("." + d)))
                .findFirst();
    }

    public boolean matchesUrl(String url) {
        return fromUrl(url).map(s -> s == this).orElse(false);
    }
}
