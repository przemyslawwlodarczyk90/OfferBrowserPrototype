package com.example.offerbrowserprototype.domain.statistics;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

// Statystyki "Wszystkie strony": poziomy sumowane ze wszystkich portali — H2 w pamięci
@DataJpaTest
class LevelBySourceDistributionHandlerTest {

    @Autowired
    private OfferRepository repository;

    private int n;

    private void offer(String source, String level) {
        repository.save(Offer.builder().title("Java").company("Firma").location("Warszawa")
                .offerUrl("https://example.com/" + (++n)).source(source).level(level)
                .fetchedAt(LocalDateTime.now()).build());
    }

    @Test
    void sumuje_poziomy_ze_wszystkich_portali_z_podzialem_na_portal() {
        offer("NoFluff", "Junior");
        offer("NoFluff", "Mid");
        offer("NoFluff", "mid");
        offer("TheProtocol", "Mid");
        offer("TheProtocol", "Nieokreślony poziom");
        offer("JustJoin", "Junior");
        offer("JustJoin", "Mid");
        offer("JustJoin", null);
        offer("JustJoin", "Senior");

        Map<String, Map<String, Long>> result = new LevelBySourceDistributionHandler(repository).getDistribution();

        assertThat(List.copyOf(result.keySet())).containsExactly("Junior", "Mid", "Senior", "Nieokreślony poziom");
        assertThat(result.get("Mid")).containsExactly(Map.entry("NoFluff", 2L), Map.entry("TheProtocol", 1L),
                Map.entry("JustJoin", 1L));
        assertThat(result.get("Junior")).containsEntry("TheProtocol", 0L).containsEntry("JustJoin", 1L);
        assertThat(result.get("Nieokreślony poziom")).containsEntry("TheProtocol", 1L).containsEntry("JustJoin", 1L);
        long total = result.values().stream().flatMap(m -> m.values().stream()).mapToLong(Long::longValue).sum();
        assertThat(total).isEqualTo(9);
    }

    @Test
    void normalizacja_nazw_poziomow() {
        assertThat(LevelBySourceDistributionHandler.normalizeLevel("junior")).isEqualTo("Junior");
        assertThat(LevelBySourceDistributionHandler.normalizeLevel(" ")).isEqualTo("Nieokreślony poziom");
        assertThat(LevelBySourceDistributionHandler.normalizeLevel("nieokreślony poziom")).isEqualTo("Nieokreślony poziom");
        assertThat(LevelBySourceDistributionHandler.normalizeLevel("Lead")).isEqualTo("Lead");
    }
}
