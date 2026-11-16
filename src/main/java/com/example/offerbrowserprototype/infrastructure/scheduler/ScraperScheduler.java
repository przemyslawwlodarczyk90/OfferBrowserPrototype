package com.example.offerbrowserprototype.infrastructure.scheduler;

import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService.Mode;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.random.RandomGenerator;

/**
 * Daily schedule for all portals (Europe/Warsaw):
 * <ol>
 *   <li>at <b>00:01</b> the day's plan is drawn ({@link DailyPlanDrawer}): one random
 *       time per portal, never yesterday's slot, at least 2 hours apart — redrawn
 *       until the gap holds;</li>
 *   <li>a once-a-minute tick starts each portal at its drawn time, once a day.</li>
 * </ol>
 * The plan is stored in a file, so a restart keeps it. If the application was not
 * running at 00:01, the plan is drawn on startup (only from times still ahead).
 */
@Component
@ConditionalOnProperty(name = "scraper.scheduling.enabled", havingValue = "true", matchIfMissing = true)
public class ScraperScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ScraperScheduler.class);
    static final ZoneId ZONE = ZoneId.of("Europe/Warsaw");
    static final LocalTime DRAW_TIME = LocalTime.of(0, 1);
    /** A run whose minute was missed (e.g. machine asleep) still starts within this window. */
    static final Duration GRACE = Duration.ofMinutes(10);
    static final List<OfferSource> PORTALS = List.of(OfferSource.values());

    /** 20 irregular base times, ≥ 41 min apart, so shifted times of different slots never overlap. */
    static final String DEFAULT_SLOTS = "06:11,06:53,07:46,08:27,09:17,10:04,10:48,11:39,12:21,13:13,"
            + "13:56,14:44,15:28,16:23,17:06,17:52,18:47,19:31,20:24,21:18";
    static final String DEFAULT_OFFSETS = "3,7,11,15,17";

    private final ScraperJobService jobService;
    private final ScraperSettings settings;
    private final DailyPlanDrawer drawer;
    private final DailyPlanStore store;
    private final RandomGenerator random;
    private final Clock clock;
    private DailyPlanStore.State state;

    @Autowired
    public ScraperScheduler(ScraperJobService jobService, ScraperSettings settings,
                            @Value("${scraper.schedule.slots:" + DEFAULT_SLOTS + "}") String slots,
                            @Value("${scraper.schedule.offsets-minutes:" + DEFAULT_OFFSETS + "}") String offsets,
                            @Value("${scraper.schedule.min-gap-minutes:120}") int minGapMinutes,
                            @Value("${scraper.schedule.plan-file:data/schedule/daily_plan.json}") String planFile) {
        this(jobService, settings, DailyPlanDrawer.parse(slots, offsets, Duration.ofMinutes(minGapMinutes)),
                new DailyPlanStore(Path.of(planFile)), new SecureRandom(), Clock.system(ZONE));
    }

    ScraperScheduler(ScraperJobService jobService, ScraperSettings settings, DailyPlanDrawer drawer,
                     DailyPlanStore store, RandomGenerator random, Clock clock) {
        this.jobService = jobService;
        this.settings = settings;
        this.drawer = drawer;
        this.store = store;
        this.random = random;
        this.clock = clock;
        this.state = store.load().orElse(new DailyPlanStore.State(null, null));
        LocalDateTime now = LocalDateTime.now(clock);
        if (!hasPlanFor(now.toLocalDate()) && !now.toLocalTime().isBefore(DRAW_TIME)) {
            logger.info("No schedule drawn for today yet (application was not running at 00:01) — drawing now");
            drawPlan(now, true);
        } else if (hasPlanFor(now.toLocalDate())) {
            logger.info("Today's scraping schedule: {}", describe(state.current()));
        }
    }

    /** 00:01 — draw the day's plan before any scraping. */
    @Scheduled(cron = "0 1 0 * * *", zone = "Europe/Warsaw")
    public synchronized void drawDailyPlan() {
        LocalDateTime now = LocalDateTime.now(clock);
        if (!hasPlanFor(now.toLocalDate())) {
            drawPlan(now, false);
        }
    }

    /** Every minute — start portals whose drawn time has come. */
    @Scheduled(cron = "0 * * * * *", zone = "Europe/Warsaw")
    public synchronized void tick() {
        LocalDateTime now = LocalDateTime.now(clock).truncatedTo(ChronoUnit.MINUTES);
        if (!hasPlanFor(now.toLocalDate())) {
            if (now.toLocalTime().isBefore(DRAW_TIME)) {
                return; // the draw happens at 00:01
            }
            drawPlan(now, true); // 00:01 was missed (e.g. machine asleep)
        }
        DailyPlan plan = state.current();
        for (Map.Entry<OfferSource, DailyPlan.Entry> e : plan.portals().entrySet()) {
            DailyPlan.Entry entry = e.getValue();
            LocalDateTime due = plan.date().atTime(entry.runAt());
            if (!entry.started() && !now.isBefore(due) && now.isBefore(due.plus(GRACE))) {
                plan = plan.markStarted(e.getKey());
                state = new DailyPlanStore.State(plan, state.previous());
                store.save(state);
                trigger(e.getKey());
            }
        }
    }

    /** Today's drawn run of the portal, if any. */
    public synchronized Optional<DailyPlan.Entry> todaysRun(OfferSource source) {
        LocalDate today = LocalDate.now(clock);
        return hasPlanFor(today) ? Optional.ofNullable(state.current().portals().get(source)) : Optional.empty();
    }

    /** Next planned automatic run of the portal (only today's plan is known — tomorrow's is drawn at 00:01). */
    public synchronized Optional<LocalDateTime> nextScheduledRun(OfferSource source) {
        LocalDate today = LocalDate.now(clock);
        return todaysRun(source)
                .filter(e -> !e.started())
                .map(e -> today.atTime(e.runAt()))
                .filter(t -> !t.plus(GRACE).isBefore(LocalDateTime.now(clock)));
    }

    private boolean hasPlanFor(LocalDate day) {
        return state.current() != null && day.equals(state.current().date());
    }

    private void drawPlan(LocalDateTime now, boolean onlyFutureTimes) {
        LocalDate today = now.toLocalDate();
        DailyPlan yesterdayPlan = state.current() != null && state.current().date().equals(today.minusDays(1))
                ? state.current()
                : state.previous() != null && state.previous().date().equals(today.minusDays(1)) ? state.previous() : null;
        Map<OfferSource, Integer> yesterday = new EnumMap<>(OfferSource.class);
        if (yesterdayPlan != null) {
            yesterdayPlan.portals().forEach((s, e) -> yesterday.put(s, e.slotIndex()));
        }

        DailyPlan plan = null;
        if (onlyFutureTimes) {
            // even the largest backward shift must still be ahead of now
            LocalTime earliest = now.toLocalTime().plusMinutes(5L + drawer.maxOffset());
            List<LocalTime> future = drawer.slots().stream().filter(t -> t.isAfter(earliest)).toList();
            try {
                DailyPlanDrawer late = new DailyPlanDrawer(future, drawer.offsets(), drawer.minGap());
                DailyPlan p = late.draw(today, PORTALS, Map.of(), random);
                // translate indices back to the full slot list so tomorrow's draw avoids them
                Map<OfferSource, DailyPlan.Entry> mapped = new EnumMap<>(OfferSource.class);
                p.portals().forEach((s, e) -> mapped.put(s, new DailyPlan.Entry(
                        drawer.slots().indexOf(e.slot()), e.slot(), e.runAt(), false)));
                plan = new DailyPlan(today, p.attempts(), mapped);
            } catch (RuntimeException tooLate) {
                logger.warn("Not enough slots left today for all portals with the required gap — today's runs are skipped");
                plan = new DailyPlan(today, 0, Map.of());
            }
        }
        if (plan == null) {
            plan = drawer.draw(today, PORTALS, yesterday, random);
        }
        state = new DailyPlanStore.State(plan, state.current() != null && !today.equals(state.current().date())
                ? state.current() : state.previous());
        store.save(state);
        logger.info("Drew scraping schedule for {} after {} attempt(s): {}", today, plan.attempts(), describe(plan));
    }

    private static String describe(DailyPlan plan) {
        StringBuilder sb = new StringBuilder();
        plan.portals().forEach((s, e) -> sb.append(s.value()).append(' ').append(e.runAt())
                .append(e.started() ? " (started)" : "").append("; "));
        return sb.isEmpty() ? "no runs" : sb.toString().trim();
    }

    void trigger(OfferSource source) {
        ScraperSettings.Portal portal = settings.of(source);
        Mode mode = portal.enabled() && portal.scrapeOnSchedule() ? Mode.SCRAPE_AND_IMPORT : Mode.IMPORT_ONLY;
        try {
            jobService.start(source, mode, "schedule").ifPresentOrElse(
                    run -> logger.info("[{}] Scheduled {} started (job {})", source.value(), mode, run.jobId()),
                    () -> logger.warn("[{}] Scheduled run skipped — previous run still in progress", source.value()));
        } catch (RuntimeException e) {
            logger.error("[{}] Scheduled run could not start: {}", source.value(), e.getMessage());
        }
    }
}
