package com.example.offerbrowserprototype.domain.scraperurl;

import com.example.offerbrowserprototype.domain.dto.scraperurl.ScraperSearchUrlDTO;
import com.example.offerbrowserprototype.domain.exception.ConflictException;
import com.example.offerbrowserprototype.domain.exception.NotFoundException;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.infrastructure.repository.ScraperSearchUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Adresy wyszukiwania scraperów — H2 w pamięci
@DataJpaTest
class ScraperSearchUrlHandlerTest {

    private static final String JJ_LODZ = "https://justjoin.it/job-offers/lodz/java?experience-levels=junior,mid";

    @Autowired
    private ScraperSearchUrlRepository repository;

    private ScraperSearchUrlHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ScraperSearchUrlHandler(repository, Clock.systemUTC());
    }

    @Test
    void pusty_portal_dostaje_domyslne_adresy_a_istniejace_listy_zostaja_bez_zmian() {
        handler.add(OfferSource.NOFLUFF, "https://nofluffjobs.com/pl/Kotlin", "własny", "admin");

        handler.seedMissingPortals();

        assertThat(handler.urlsFor(OfferSource.NOFLUFF)).containsExactly("https://nofluffjobs.com/pl/Kotlin");
        assertThat(handler.urlsFor(OfferSource.THEPROTOCOL)).hasSize(DefaultSearchUrls.of(OfferSource.THEPROTOCOL).size());
        assertThat(handler.urlsFor(OfferSource.JUSTJOIN)).hasSize(3).contains(JJ_LODZ);
        assertThat(handler.list(OfferSource.JUSTJOIN)).allMatch(ScraperSearchUrlDTO::isDefault);
    }

    @Test
    void dodaje_adres_portalu_i_zachowuje_kolejnosc() {
        handler.add(OfferSource.JUSTJOIN, " " + JJ_LODZ + " ", "  Łódź  ", "admin");
        ScraperSearchUrlDTO added = handler.add(OfferSource.JUSTJOIN,
                "https://justjoin.it/job-offers/krakow/java?experience-levels=junior", null, "admin");

        assertThat(handler.urlsFor(OfferSource.JUSTJOIN))
                .containsExactly(JJ_LODZ, "https://justjoin.it/job-offers/krakow/java?experience-levels=junior");
        assertThat(handler.list(OfferSource.JUSTJOIN).get(0).label()).isEqualTo("Łódź");
        assertThat(added.isDefault()).isFalse();
        assertThat(added.createdBy()).isEqualTo("admin");
        assertThat(handler.urlsFor(OfferSource.NOFLUFF)).isEmpty();
    }

    @Test
    void odrzuca_adres_innego_portalu_i_niepoprawne_adresy() {
        assertThatThrownBy(() -> handler.add(OfferSource.JUSTJOIN, "https://theprotocol.it/filtry/java;t", null, "admin"))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("justjoin.it");
        assertThatThrownBy(() -> handler.add(OfferSource.JUSTJOIN, "https://justjoin.it.evil.com/x", null, "admin"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> handler.add(OfferSource.JUSTJOIN, "ftp://justjoin.it/x", null, "admin"))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("https://");
        assertThatThrownBy(() -> handler.add(OfferSource.JUSTJOIN, "https://justjoin.it/a b", null, "admin"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> handler.add(OfferSource.JUSTJOIN, "  ", null, "admin"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(repository.count()).isZero();
    }

    @Test
    void duplikat_w_tym_samym_portalu_to_konflikt() {
        handler.add(OfferSource.JUSTJOIN, JJ_LODZ, null, "admin");

        assertThatThrownBy(() -> handler.add(OfferSource.JUSTJOIN, JJ_LODZ, "znowu", "admin"))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void podmiana_adresu_na_nowe_parametry() {
        Long id = handler.add(OfferSource.JUSTJOIN, JJ_LODZ, "Łódź", "admin").id();
        String other = handler.add(OfferSource.JUSTJOIN, "https://justjoin.it/job-offers/warszawa/java", null, "admin").url();

        ScraperSearchUrlDTO updated = handler.update(id,
                "https://justjoin.it/job-offers/lodz/java?experience-levels=junior", "Łódź – tylko junior", "admin");

        assertThat(updated.url()).endsWith("experience-levels=junior");
        assertThat(updated.label()).isEqualTo("Łódź – tylko junior");
        assertThat(updated.updatedAt()).isNotNull();
        assertThatThrownBy(() -> handler.update(id, other, null, "admin")).isInstanceOf(ConflictException.class);
        assertThatThrownBy(() -> handler.update(id, "https://nofluffjobs.com/pl/Java", null, "admin"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> handler.update(9999L, JJ_LODZ, null, "admin")).isInstanceOf(NotFoundException.class);
    }

    @Test
    void ostatniego_adresu_portalu_nie_mozna_usunac() {
        Long first = handler.add(OfferSource.JUSTJOIN, JJ_LODZ, null, "admin").id();
        Long second = handler.add(OfferSource.JUSTJOIN, "https://justjoin.it/job-offers/warszawa/java", null, "admin").id();

        handler.delete(first, "admin");

        assertThat(handler.urlsFor(OfferSource.JUSTJOIN)).containsExactly("https://justjoin.it/job-offers/warszawa/java");
        assertThatThrownBy(() -> handler.delete(second, "admin")).isInstanceOf(ConflictException.class);
        assertThat(handler.urlsFor(OfferSource.JUSTJOIN)).hasSize(1);
    }

    @Test
    void przywrocenie_domyslnych_dodaje_tylko_brakujace() {
        handler.add(OfferSource.JUSTJOIN, JJ_LODZ, "moja etykieta", "admin");

        int added = handler.restoreDefaults(OfferSource.JUSTJOIN, "admin");

        assertThat(added).isEqualTo(2);
        assertThat(handler.urlsFor(OfferSource.JUSTJOIN)).hasSize(3).startsWith(JJ_LODZ);
        assertThat(handler.list(OfferSource.JUSTJOIN).get(0).label()).isEqualTo("moja etykieta");
        assertThat(handler.restoreDefaults(OfferSource.JUSTJOIN, "admin")).isZero();
    }
}
