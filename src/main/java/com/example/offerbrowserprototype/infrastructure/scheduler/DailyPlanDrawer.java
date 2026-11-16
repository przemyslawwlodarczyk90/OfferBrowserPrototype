package com.example.offerbrowserprototype.infrastructure.scheduler;

import com.example.offerbrowserprototype.domain.offer.OfferSource;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;

/**
 * Draws one day's scraping schedule: for every portal a random slot out of the
 * configured irregular times (never the slot it had the day before), shifted
 * forward or backward by one of the configured offsets (e.g. 3/7/11/15/17 min —
 * never 0). Portals must be at least {@code minGap} apart — when they are not,
 * the whole draw is repeated.
 */
public class DailyPlanDrawer {

    static final int MAX_ATTEMPTS = 10_000;

    private final List<LocalTime> slots;
    /** Shift magnitudes in minutes; each is applied forward or backward at random. Empty = no shift. */
    private final List<Integer> offsets;
    private final Duration minGap;

    public DailyPlanDrawer(List<LocalTime> slots, List<Integer> offsets, Duration minGap) {
        if (slots.size() < 2) {
            throw new IllegalArgumentException("Potrzebne są co najmniej 2 pory, żeby je losować");
        }
        if (offsets.stream().anyMatch(o -> o <= 0) || minGap.isNegative()) {
            throw new IllegalArgumentException("Przesunięcia muszą być dodatnie, a odstęp nieujemny");
        }
        this.slots = slots.stream().sorted().distinct().toList();
        this.offsets = offsets.stream().distinct().sorted().toList();
        this.minGap = minGap;
    }

    public static DailyPlanDrawer parse(String slotsCsv, String offsetsCsv, Duration minGap) {
        List<LocalTime> slots = Arrays.stream(slotsCsv.split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).map(LocalTime::parse).toList();
        List<Integer> offsets = offsetsCsv == null ? List.of() : Arrays.stream(offsetsCsv.split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).map(Integer::parseInt).toList();
        return new DailyPlanDrawer(slots, offsets, minGap);
    }

    /**
     * @param yesterday slot index each portal had the previous day (missing = no restriction)
     * @throws IllegalStateException when the configuration makes the gap impossible
     */
    public DailyPlan draw(LocalDate day, List<OfferSource> portals, Map<OfferSource, Integer> yesterday,
                          RandomGenerator random) {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            Map<OfferSource, DailyPlan.Entry> entries = new EnumMap<>(OfferSource.class);
            for (OfferSource portal : portals) {
                int index = drawSlot(yesterday.get(portal), random);
                entries.put(portal, new DailyPlan.Entry(index, slots.get(index), jitter(slots.get(index), random), false));
            }
            if (gapsOk(new ArrayList<>(entries.values()))) {
                return new DailyPlan(day, attempt, entries);
            }
        }
        throw new IllegalStateException("Nie da się wylosować harmonogramu z odstępem " + minGap.toMinutes()
                + " min — za mało pór w scraper.schedule.slots");
    }

    private int drawSlot(Integer yesterdayIndex, RandomGenerator random) {
        if (yesterdayIndex == null || yesterdayIndex < 0 || yesterdayIndex >= slots.size()) {
            return random.nextInt(slots.size());
        }
        // any slot except yesterday's
        int i = random.nextInt(slots.size() - 1);
        return i >= yesterdayIndex ? i + 1 : i;
    }

    private LocalTime jitter(LocalTime slot, RandomGenerator random) {
        if (offsets.isEmpty()) {
            return slot;
        }
        int shift = offsets.get(random.nextInt(offsets.size())) * (random.nextBoolean() ? 1 : -1);
        int minute = slot.getHour() * 60 + slot.getMinute() + shift;
        minute = Math.max(0, Math.min(minute, 24 * 60 - 1));
        return LocalTime.of(minute / 60, minute % 60);
    }

    private boolean gapsOk(List<DailyPlan.Entry> entries) {
        for (int i = 0; i < entries.size(); i++) {
            for (int j = i + 1; j < entries.size(); j++) {
                Duration gap = Duration.between(entries.get(i).runAt(), entries.get(j).runAt()).abs();
                if (gap.compareTo(minGap) < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<LocalTime> slots() {
        return slots;
    }

    public Duration minGap() {
        return minGap;
    }

    public List<Integer> offsets() {
        return offsets;
    }

    /** Largest possible shift, in minutes. */
    public int maxOffset() {
        return offsets.isEmpty() ? 0 : offsets.get(offsets.size() - 1);
    }
}
