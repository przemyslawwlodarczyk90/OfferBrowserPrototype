package com.example.offerbrowserprototype.infrastructure.scheduler;

import com.example.offerbrowserprototype.domain.offer.OfferSource;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DailyPlanDrawerTest {

    private static final List<OfferSource> PORTALS = List.of(OfferSource.NOFLUFF, OfferSource.THEPROTOCOL);
    private static final LocalDate DAY = LocalDate.of(2026, 10, 12);
    private final DailyPlanDrawer drawer =
            DailyPlanDrawer.parse(ScraperScheduler.DEFAULT_SLOTS, ScraperScheduler.DEFAULT_OFFSETS, Duration.ofHours(2));

    private static Duration gap(DailyPlan p) {
        return Duration.between(p.portals().get(OfferSource.NOFLUFF).runAt(),
                p.portals().get(OfferSource.THEPROTOCOL).runAt()).abs();
    }

    @Test
    void dwadziescia_nieregularnych_por_oddalonych_tak_ze_przesuniecia_sie_nie_nakladaja() {
        assertThat(drawer.slots()).hasSize(20).noneMatch(t -> t.getMinute() == 0 || t.getMinute() == 30);
        for (int i = 1; i < drawer.slots().size(); i++) {
            long gap = Duration.between(drawer.slots().get(i - 1), drawer.slots().get(i)).toMinutes();
            assertThat(gap).isGreaterThan(2L * drawer.maxOffset());
        }
    }

    @Test
    void portale_zawsze_co_najmniej_2_godziny_od_siebie() {
        Random random = new Random(1);
        for (int i = 0; i < 5000; i++) {
            DailyPlan plan = drawer.draw(DAY, PORTALS, Map.of(), random);
            assertThat(gap(plan)).isGreaterThanOrEqualTo(Duration.ofHours(2));
        }
    }

    @Test
    void zbyt_maly_odstep_powoduje_ponowne_losowanie() {
        Random random = new Random(7);
        int redrawn = 0;
        for (int i = 0; i < 500; i++) {
            if (drawer.draw(DAY, PORTALS, Map.of(), random).attempts() > 1) redrawn++;
        }
        assertThat(redrawn).isGreaterThan(50); // ok. 1/4 losowań wypada za blisko i jest powtarzana
    }

    @Test
    void zaden_portal_nie_dostaje_wczorajszej_pory() {
        Random random = new Random(3);
        Map<OfferSource, Integer> yesterday = new EnumMap<>(OfferSource.class);
        yesterday.put(OfferSource.NOFLUFF, 4);
        yesterday.put(OfferSource.THEPROTOCOL, 11);
        for (int i = 0; i < 3000; i++) {
            DailyPlan plan = drawer.draw(DAY, PORTALS, yesterday, random);
            assertThat(plan.portals().get(OfferSource.NOFLUFF).slotIndex()).isNotEqualTo(4);
            assertThat(plan.portals().get(OfferSource.THEPROTOCOL).slotIndex()).isNotEqualTo(11);
        }
    }

    @Test
    void przesuniecie_tylko_o_3_7_11_15_lub_17_minut_w_przod_albo_wstecz() {
        Random random = new Random(5);
        Set<Long> shifts = new HashSet<>();
        Set<Integer> usedSlots = new HashSet<>();
        Set<LocalTime> distinctTimes = new HashSet<>();
        for (int i = 0; i < 20_000; i++) {
            DailyPlan.Entry e = drawer.draw(DAY, PORTALS, Map.of(), random).portals().get(OfferSource.THEPROTOCOL);
            shifts.add(Duration.between(e.slot(), e.runAt()).toMinutes());
            usedSlots.add(e.slotIndex());
            distinctTimes.add(e.runAt());
        }
        assertThat(shifts).containsExactlyInAnyOrder(-17L, -15L, -11L, -7L, -3L, 3L, 7L, 11L, 15L, 17L);
        assertThat(usedSlots).hasSize(20);
        assertThat(distinctTimes).hasSize(200); // 20 pór × 10 przesunięć, nigdy dokładnie o pełnej porze
        assertThat(distinctTimes).doesNotContainAnyElementsOf(drawer.slots());
    }

    @Test
    void przesuniecia_musza_byc_dodatnie() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> DailyPlanDrawer.parse("10:00,12:00", "3,0", Duration.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void niemozliwy_odstep_konczy_sie_czytelnym_bledem() {
        DailyPlanDrawer tooTight = DailyPlanDrawer.parse("10:00,10:30,11:00", "", Duration.ofHours(2));
        assertThatThrownBy(() -> tooTight.draw(DAY, PORTALS, Map.of(), new Random(1)))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("odstęp");
    }
}
