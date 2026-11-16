package com.example.offerbrowserprototype.infrastructure.scheduler;

import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService.Mode;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.scheduling.annotation.Scheduled;

import java.nio.file.Path;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ScraperSchedulerTest {

    @TempDir Path tmp;

    private ScraperJobService jobs;
    private DailyPlanStore store;
    private MutableClock clock;
    private final DailyPlanDrawer drawer = DailyPlanDrawer.parse(ScraperScheduler.DEFAULT_SLOTS, ScraperScheduler.DEFAULT_OFFSETS, Duration.ofHours(2));

    /** Clock the test can move forward minute by minute. */
    static final class MutableClock extends Clock {
        private Instant now;
        MutableClock(LocalDateTime start) { this.now = start.atZone(ScraperScheduler.ZONE).toInstant(); }
        void set(LocalDateTime t) { now = t.atZone(ScraperScheduler.ZONE).toInstant(); }
        LocalDateTime local() { return LocalDateTime.ofInstant(now, ScraperScheduler.ZONE); }
        @Override public java.time.ZoneId getZone() { return ScraperScheduler.ZONE; }
        @Override public Clock withZone(java.time.ZoneId zone) { return this; }
        @Override public Instant instant() { return now; }
    }

    private static ScraperSettings settings(boolean tpEnabled) {
        return new ScraperSettings("nf.py", "nfo.py", "nf.json", 5, true, true,
                "tp.py", "tpo.py", "tp.json", 5, tpEnabled, true, 60);
    }

    @BeforeEach
    void setUp() {
        jobs = mock(ScraperJobService.class);
        when(jobs.start(any(), any(), anyString())).thenReturn(Optional.empty());
        store = new DailyPlanStore(tmp.resolve("plan.json"));
        clock = new MutableClock(LocalDateTime.of(2026, 10, 12, 0, 0));
    }

    private ScraperScheduler scheduler(boolean tpEnabled) {
        return new ScraperScheduler(jobs, settings(tpEnabled), drawer, store, new Random(42), clock);
    }

    /** Runs the scheduler's crons for every minute of the day, like Spring would. */
    private void simulateDay(ScraperScheduler s, LocalDate day) {
        for (LocalDateTime t = day.atStartOfDay(); t.toLocalDate().equals(day); t = t.plusMinutes(1)) {
            clock.set(t);
            if (t.toLocalTime().equals(ScraperScheduler.DRAW_TIME)) s.drawDailyPlan();
            s.tick();
        }
    }

    @Test
    void losowanie_o_00_01_i_zegar_co_minute() throws Exception {
        assertThat(ScraperScheduler.class.getMethod("drawDailyPlan").getAnnotation(Scheduled.class).cron()).isEqualTo("0 1 0 * * *");
        assertThat(ScraperScheduler.class.getMethod("tick").getAnnotation(Scheduled.class).cron()).isEqualTo("0 * * * * *");
        assertThat(ScraperScheduler.class.getMethod("drawDailyPlan").getAnnotation(Scheduled.class).zone()).isEqualTo("Europe/Warsaw");
    }

    @Test
    void przed_00_01_nie_ma_planu_a_o_00_01_jest_wylosowany() {
        ScraperScheduler s = scheduler(true);
        assertThat(s.todaysRun(OfferSource.NOFLUFF)).isEmpty();

        clock.set(LocalDateTime.of(2026, 10, 12, 0, 1));
        s.drawDailyPlan();

        DailyPlan.Entry nf = s.todaysRun(OfferSource.NOFLUFF).orElseThrow();
        DailyPlan.Entry tp = s.todaysRun(OfferSource.THEPROTOCOL).orElseThrow();
        assertThat(Duration.between(nf.runAt(), tp.runAt()).abs()).isGreaterThanOrEqualTo(Duration.ofHours(2));
        verifyNoInteractions(jobs); // losowanie nie scrapuje
    }

    @Test
    void kazdy_portal_startuje_raz_dziennie_o_wylosowanej_godzinie_przez_tydzien() {
        ScraperScheduler s = scheduler(true);
        LocalDate day = LocalDate.of(2026, 10, 12);
        Integer previousNf = null;
        for (int i = 0; i < 7; i++) {
            simulateDay(s, day.plusDays(i));
            int nf = s.todaysRun(OfferSource.NOFLUFF).orElseThrow().slotIndex();
            if (previousNf != null) assertThat(nf).isNotEqualTo(previousNf);
            previousNf = nf;
        }
        verify(jobs, times(7)).start(OfferSource.NOFLUFF, Mode.SCRAPE_AND_IMPORT, "schedule");
        verify(jobs, times(7)).start(OfferSource.THEPROTOCOL, Mode.SCRAPE_AND_IMPORT, "schedule");
    }

    @Test
    void start_dokladnie_o_wylosowanej_minucie() {
        ScraperScheduler s = scheduler(true);
        clock.set(LocalDateTime.of(2026, 10, 12, 0, 1));
        s.drawDailyPlan();
        LocalDateTime due = LocalDate.of(2026, 10, 12).atTime(s.todaysRun(OfferSource.THEPROTOCOL).orElseThrow().runAt());

        clock.set(due.minusMinutes(1));
        s.tick();
        verify(jobs, never()).start(eq(OfferSource.THEPROTOCOL), any(), anyString());

        clock.set(due);
        s.tick();
        s.tick();
        verify(jobs, times(1)).start(OfferSource.THEPROTOCOL, Mode.SCRAPE_AND_IMPORT, "schedule");
        assertThat(s.todaysRun(OfferSource.THEPROTOCOL).orElseThrow().started()).isTrue();
    }

    @Test
    void restart_w_ciagu_dnia_zachowuje_plan_i_nie_uruchamia_drugi_raz() {
        ScraperScheduler s = scheduler(true);
        clock.set(LocalDateTime.of(2026, 10, 12, 0, 1));
        s.drawDailyPlan();
        DailyPlan.Entry tp = s.todaysRun(OfferSource.THEPROTOCOL).orElseThrow();
        clock.set(LocalDate.of(2026, 10, 12).atTime(tp.runAt()));
        s.tick();

        ScraperScheduler restarted = scheduler(true);
        assertThat(restarted.todaysRun(OfferSource.THEPROTOCOL).orElseThrow().runAt()).isEqualTo(tp.runAt());
        restarted.tick();
        verify(jobs, times(1)).start(eq(OfferSource.THEPROTOCOL), any(), anyString());
    }

    @Test
    void start_aplikacji_po_00_01_bez_planu_losuje_tylko_przyszle_godziny() {
        clock.set(LocalDateTime.of(2026, 10, 12, 13, 0));
        ScraperScheduler s = scheduler(true);

        DailyPlan.Entry nf = s.todaysRun(OfferSource.NOFLUFF).orElseThrow();
        DailyPlan.Entry tp = s.todaysRun(OfferSource.THEPROTOCOL).orElseThrow();
        assertThat(nf.runAt()).isAfter(java.time.LocalTime.of(13, 0));
        assertThat(tp.runAt()).isAfter(java.time.LocalTime.of(13, 0));
        assertThat(Duration.between(nf.runAt(), tp.runAt()).abs()).isGreaterThanOrEqualTo(Duration.ofHours(2));
    }

    @Test
    void wylaczony_scraper_o_swojej_godzinie_tylko_importuje_plik() {
        ScraperScheduler s = scheduler(false);
        simulateDay(s, LocalDate.of(2026, 10, 12));
        verify(jobs).start(OfferSource.THEPROTOCOL, Mode.IMPORT_ONLY, "schedule");
    }

    @Test
    void blad_jednego_portalu_nie_przerywa_drugiego() {
        when(jobs.start(eq(OfferSource.NOFLUFF), any(), anyString())).thenThrow(new IllegalStateException("boom"));
        ScraperScheduler s = scheduler(true);
        simulateDay(s, LocalDate.of(2026, 10, 12));
        verify(jobs).start(OfferSource.THEPROTOCOL, Mode.SCRAPE_AND_IMPORT, "schedule");
    }
}
