package com.example.offerbrowserprototype.domain.statistics;

import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * "All portals" statistics: offers per experience level summed over every portal,
 * with each portal's share. Level names are unified ("mid" = "Mid"), so the same
 * level from different portals lands in one row.
 */
@Component
public class LevelBySourceDistributionHandler {

    public static final String UNKNOWN_LEVEL = "Nieokreślony poziom";
    static final List<String> LEVEL_ORDER = List.of("Trainee", "Junior", "Mid", "Senior", "Expert", "Manager");

    private final OfferRepository offerRepository;

    public LevelBySourceDistributionHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    /** level → (portal → offers); levels in seniority order, unknown level last; every portal present (0 if none). */
    public Map<String, Map<String, Long>> getDistribution() {
        Map<String, Map<String, Long>> byLevel = new TreeMap<>(LEVEL_COMPARATOR);
        for (SourceLevelCountProjection row : offerRepository.getLevelDistributionPerSource()) {
            String source = row.getSource() == null ? "Inne" : row.getSource();
            byLevel.computeIfAbsent(normalizeLevel(row.getLevel()), k -> emptyPortals())
                    .merge(source, row.getCount() == null ? 0L : row.getCount(), Long::sum);
        }
        return new LinkedHashMap<>(byLevel);
    }

    static String normalizeLevel(String level) {
        if (level == null || level.isBlank()) {
            return UNKNOWN_LEVEL;
        }
        String trimmed = level.trim();
        return LEVEL_ORDER.stream().filter(l -> l.equalsIgnoreCase(trimmed)).findFirst()
                .orElse(trimmed.toLowerCase(Locale.ROOT).startsWith("nieokre") ? UNKNOWN_LEVEL : trimmed);
    }

    private static Map<String, Long> emptyPortals() {
        Map<String, Long> m = new LinkedHashMap<>();
        for (OfferSource s : OfferSource.values()) {
            m.put(s.value(), 0L);
        }
        return m;
    }

    private static final Comparator<String> LEVEL_COMPARATOR = Comparator
            .comparingInt((String l) -> {
                int i = LEVEL_ORDER.indexOf(l);
                return i >= 0 ? i : UNKNOWN_LEVEL.equals(l) ? Integer.MAX_VALUE : LEVEL_ORDER.size();
            })
            .thenComparing(Comparator.naturalOrder());
}
