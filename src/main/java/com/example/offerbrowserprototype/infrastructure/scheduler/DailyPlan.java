package com.example.offerbrowserprototype.infrastructure.scheduler;

import com.example.offerbrowserprototype.domain.offer.OfferSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;

/** One day's drawn schedule: when each portal is scraped and whether it has started. */
public record DailyPlan(LocalDate date, int attempts, Map<OfferSource, Entry> portals) {

    /** @param slotIndex index in the configured slot list (used to avoid repeating it tomorrow) */
    public record Entry(int slotIndex, LocalTime slot, LocalTime runAt, boolean started) {}

    public DailyPlan markStarted(OfferSource source) {
        Map<OfferSource, Entry> copy = new EnumMap<>(portals);
        Entry e = copy.get(source);
        if (e != null) {
            copy.put(source, new Entry(e.slotIndex(), e.slot(), e.runAt(), true));
        }
        return new DailyPlan(date, attempts, copy);
    }
}
