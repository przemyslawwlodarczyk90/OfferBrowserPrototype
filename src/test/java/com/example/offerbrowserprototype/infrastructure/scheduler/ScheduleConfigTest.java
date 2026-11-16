package com.example.offerbrowserprototype.infrastructure.scheduler;

import com.example.offerbrowserprototype.domain.offer.OfferSource;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

// Pilnuje, żeby ręcznie edytowany harmonogram w application.properties dało się wczytać i losować
class ScheduleConfigTest {

    private static Properties load() throws Exception {
        Properties p = new Properties();
        try (InputStream in = ScheduleConfigTest.class.getClassLoader().getResourceAsStream("application.properties")) {
            p.load(in);
        }
        return p;
    }

    @Test
    void godziny_i_przesuniecia_z_application_properties_sa_poprawne() throws Exception {
        Properties p = load();
        DailyPlanDrawer drawer = DailyPlanDrawer.parse(
                p.getProperty("scraper.schedule.slots"),
                p.getProperty("scraper.schedule.offsets-minutes"),
                Duration.ofMinutes(Long.parseLong(p.getProperty("scraper.schedule.min-gap-minutes").trim())));

        assertThat(drawer.slots()).hasSize(20);
        assertThat(drawer.offsets()).containsExactly(3, 7, 11, 15, 17);
        // sąsiednie godziny dalej niż 2 x największe przesunięcie — przesunięte terminy się nie pokrywają
        for (int i = 1; i < drawer.slots().size(); i++) {
            assertThat(Duration.between(drawer.slots().get(i - 1), drawer.slots().get(i)).toMinutes())
                    .isGreaterThan(2L * drawer.maxOffset());
        }
        // da się wylosować plan z wymaganym odstępem
        DailyPlan plan = drawer.draw(java.time.LocalDate.of(2026, 10, 12),
                List.of(OfferSource.NOFLUFF, OfferSource.THEPROTOCOL), Map.of(), new Random(1));
        assertThat(plan.portals()).hasSize(2);
    }
}
