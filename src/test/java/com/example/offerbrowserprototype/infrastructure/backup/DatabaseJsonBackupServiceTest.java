package com.example.offerbrowserprototype.infrastructure.backup;

import com.example.offerbrowserprototype.domain.exception.NotFoundException;
import com.example.offerbrowserprototype.domain.offer.FlagType;
import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.offer.OfferFlag;
import com.example.offerbrowserprototype.domain.scraperurl.ScraperSearchUrl;
import com.example.offerbrowserprototype.domain.user.Role;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferStatus;
import com.example.offerbrowserprototype.infrastructure.repository.OfferFlagRepository;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.ScraperSearchUrlRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import com.example.offerbrowserprototype.infrastructure.backup.DatabaseJsonBackupService.BackupInfo;
import com.example.offerbrowserprototype.infrastructure.backup.DatabaseJsonBackupService.RestoreResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

// Kopia całej bazy do JSON i przywracanie — H2 w pamięci, dane zapisywane naprawdę (bez transakcji testu)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class DatabaseJsonBackupServiceTest {

    @TempDir Path dir;

    @Autowired DataSource dataSource;
    @Autowired UserRepository users;
    @Autowired OfferRepository offers;
    @Autowired OfferFlagRepository flags;
    @Autowired UserOfferStatusRepository statuses;
    @Autowired ScraperSearchUrlRepository urls;
    @Autowired TransactionTemplate tx;

    private DatabaseJsonBackupService service;
    private Long offerId;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        service = new DatabaseJsonBackupService(dataSource, new ObjectMapper(),
                mock(ObjectProvider.class), mock(ObjectProvider.class), dir.toString());
        clean();
        User ola = new User();
        ola.setUsername("ola");
        ola.setEmail("ola@example.com");
        ola.setPassword("$2a$10$hash");
        ola.setActive(true);
        ola.setRole(Role.ADMIN);
        ola.setCvUrl("./cv_1_20261022-120000_CV.pdf");
        users.save(ola);
        Offer offer = offers.save(Offer.builder().title("Java Developer – Łódź").company("Firma \"ABC\"")
                .description("Opis\nw dwóch liniach").location("Łódź, praca hybrydowa").level("Junior")
                .offerUrl("https://justjoin.it/job-offer/firma-java-lodz").source("JustJoin")
                .fetchedAt(LocalDateTime.of(2026, 10, 22, 12, 30, 15, 123456000))
                .requirements(List.of("Java", "Spring Boot")).niceToHave(List.of("Docker")).build());
        offerId = offer.getId();
        flags.save(OfferFlag.builder().user(ola).offer(offer).type(FlagType.USELESS)
                .flaggedAt(LocalDateTime.of(2026, 10, 22, 13, 0)).build());
        UserOfferStatus st = new UserOfferStatus(ola, offer, true);
        st.setAppliedAt(LocalDateTime.of(2026, 10, 22, 14, 0));
        statuses.save(st);
        urls.save(ScraperSearchUrl.builder().source("JustJoin").url("https://justjoin.it/job-offers/lodz/java")
                .label("Łódź").createdAt(LocalDateTime.of(2026, 10, 1, 8, 0)).build());
    }

    @AfterEach
    void clean() {
        statuses.deleteAll();
        flags.deleteAll();
        offers.deleteAll();
        urls.deleteAll();
        users.deleteAll();
    }

    @Test
    void kopia_zawiera_wszystkie_tabele_i_trafia_do_katalogu_z_properties() throws Exception {
        BackupInfo info = service.createBackup("admin", "manual");

        assertThat(Files.exists(dir.resolve(info.fileName()))).isTrue();
        assertThat(info.fileName()).startsWith("backup_").endsWith("_manual.json");
        assertThat(info.createdBy()).isEqualTo("admin");
        assertThat(info.rowCounts().keySet()).extracting(String::toLowerCase).contains(
                "users", "job_offers", "offer_requirements", "offer_nice_to_have", "offer_flags",
                "user_offer_status", "scraper_search_urls");
        assertThat(info.rowCounts().entrySet()).filteredOn(e -> e.getKey().equalsIgnoreCase("offer_requirements"))
                .first().extracting(java.util.Map.Entry::getValue).isEqualTo(2L);
        assertThat(service.list()).extracting(BackupInfo::fileName).containsExactly(info.fileName());
        String json = Files.readString(dir.resolve(info.fileName()));
        assertThat(json).contains("\"format\" : \"offerbrowser-db-backup\"", "Firma \\\"ABC\\\"", "Java Developer – Łódź");
    }

    @Test
    void przywrocenie_odtwarza_dane_po_zmianach_i_robi_kopie_bezpieczenstwa() {
        BackupInfo backup = service.createBackup("admin", "manual");

        // zmiany po kopii: nowa oferta, usunięta flaga, zmieniony użytkownik, nowy adres
        offers.save(Offer.builder().title("Nowa").company("X").offerUrl("https://nofluffjobs.com/pl/job/nowa")
                .source("NoFluff").fetchedAt(LocalDateTime.now()).build());
        flags.deleteAll();
        tx.executeWithoutResult(s -> users.findByUsername("ola").orElseThrow().setEmail("zmieniony@example.com"));
        urls.save(ScraperSearchUrl.builder().source("NoFluff").url("https://nofluffjobs.com/pl/Kotlin")
                .createdAt(LocalDateTime.now()).build());

        RestoreResult result = service.restore(backup.fileName(), "admin");

        assertThat(result.safetyBackup()).endsWith("_pre-restore.json");
        assertThat(service.list()).hasSize(2);
        assertThat(offers.findAll()).extracting(Offer::getOfferUrl).containsExactly("https://justjoin.it/job-offer/firma-java-lodz");
        tx.executeWithoutResult(s -> {
            Offer o = offers.findById(offerId).orElseThrow();
            assertThat(o.getRequirements()).containsExactlyInAnyOrder("Java", "Spring Boot");
            assertThat(o.getNiceToHave()).containsExactly("Docker");
            assertThat(o.getFetchedAt()).isEqualTo(LocalDateTime.of(2026, 10, 22, 12, 30, 15, 123456000));
            assertThat(o.getDescription()).isEqualTo("Opis\nw dwóch liniach");
        });
        User ola = users.findByUsername("ola").orElseThrow();
        assertThat(ola.getEmail()).isEqualTo("ola@example.com");
        assertThat(ola.getRole()).isEqualTo(Role.ADMIN);
        assertThat(ola.getCvUrl()).isEqualTo("./cv_1_20261022-120000_CV.pdf");
        assertThat(flags.findAll()).hasSize(1);
        assertThat(statuses.findAll()).singleElement().matches(UserOfferStatus::isApplied);
        assertThat(urls.findAll()).extracting(ScraperSearchUrl::getUrl).containsExactly("https://justjoin.it/job-offers/lodz/java");

        // sekwencje przesunięte za odtworzone id — nowe wiersze nie kolidują
        Offer next = offers.save(Offer.builder().title("Po przywróceniu").company("Y")
                .offerUrl("https://theprotocol.it/szczegoly/praca/x,oferta,1").fetchedAt(LocalDateTime.now()).build());
        assertThat(next.getId()).isGreaterThan(offerId);
    }

    @Test
    void uszkodzona_kopia_nie_zmienia_bazy() throws Exception {
        BackupInfo backup = service.createBackup("admin", "manual");
        Path file = dir.resolve(backup.fileName());
        // wiersz z błędną datą — błąd dopiero w trakcie wstawiania, po usunięciu danych → musi być rollback
        String broken = Files.readString(file).replace("2026-10-22T13:00", "to-nie-data");
        Files.writeString(file, broken);
        long before = offers.count();

        assertThatThrownBy(() -> service.restore(backup.fileName(), "admin"))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Niepoprawna wartość");

        assertThat(offers.count()).isEqualTo(before);
        assertThat(flags.count()).isEqualTo(1);
        assertThat(users.findByUsername("ola")).isPresent();
    }

    @Test
    void obcy_plik_jest_odrzucany_przy_wgrywaniu() {
        assertThatThrownBy(() -> service.storeUpload("dane.json",
                new ByteArrayInputStream("{\"cos\": 1}".getBytes(StandardCharsets.UTF_8))))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("kopii");
        assertThat(service.list()).isEmpty();
    }

    @Test
    void wgrana_kopia_trafia_do_katalogu_i_mozna_ja_przywrocic() throws Exception {
        BackupInfo backup = service.createBackup("admin", "manual");
        byte[] content = Files.readAllBytes(dir.resolve(backup.fileName()));
        statuses.deleteAll();
        flags.deleteAll();
        offers.deleteAll();

        String stored = service.storeUpload("../../moja kopia.json", new ByteArrayInputStream(content));
        service.restore(stored, "admin");

        assertThat(stored).startsWith("uploaded_").endsWith("_moja_kopia.json");
        assertThat(Files.exists(dir.resolve(stored))).isTrue();
        assertThat(offers.count()).isEqualTo(1);
        assertThat(flags.count()).isEqualTo(1);
        assertThat(statuses.count()).isEqualTo(1);
    }

    @Test
    void nazwy_plikow_nie_wychodza_poza_katalog() {
        assertThatThrownBy(() -> service.file("../application.properties")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.file("..\\x.json")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.file("brak.json")).isInstanceOf(NotFoundException.class);
    }
}
