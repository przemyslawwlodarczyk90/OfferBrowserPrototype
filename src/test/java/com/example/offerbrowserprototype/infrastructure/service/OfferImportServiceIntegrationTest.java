package com.example.offerbrowserprototype.infrastructure.service;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.domain.requirement.NiceToHaveCreationHandler;
import com.example.offerbrowserprototype.domain.requirement.RequirementCreationHandler;
import com.example.offerbrowserprototype.domain.user.Role;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferStatus;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheInvalidator;
import com.example.offerbrowserprototype.infrastructure.config.OfferSourceBackfill;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.RequirementRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import com.example.offerbrowserprototype.infrastructure.service.OfferImportService.ImportResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

// Import obu portali na prawdziwych plikach wygenerowanych przez scrapery (H2 w pamięci)
@DataJpaTest
@Import({OfferImportService.class, RequirementCreationHandler.class, NiceToHaveCreationHandler.class,
        OfferCacheInvalidator.class, OfferSourceBackfill.class, OfferImportServiceIntegrationTest.Config.class})
class OfferImportServiceIntegrationTest {

    @TestConfiguration
    static class Config {
        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Autowired private OfferImportService importService;
    @Autowired private OfferRepository offerRepository;
    @Autowired private RequirementRepository requirementRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserOfferStatusRepository statusRepository;
    @Autowired private OfferSourceBackfill backfill;

    private static String file(String name) throws Exception {
        return new ClassPathResource("import/" + name).getFile().getAbsolutePath();
    }

    @Test
    void importuje_prawdziwy_plik_theprotocol_z_poprawnym_zrodlem() throws Exception {
        ImportResult result = importService.importOffersFromJson(file("theprotocol_offers_sample.json"), OfferSource.THEPROTOCOL);

        assertThat(result.imported()).isEqualTo(5);
        assertThat(result.rejected()).isZero();
        List<Offer> saved = offerRepository.findBySourceOrderByFetchedAtDesc("TheProtocol");
        assertThat(saved).hasSize(5);
        assertThat(saved).allSatisfy(o -> {
            assertThat(o.getOfferUrl()).startsWith("https://theprotocol.it/szczegoly/praca/");
            assertThat(o.getCompany()).isNotBlank();
            assertThat(o.getFetchedAt()).isNotNull();
            assertThat(o.getCity()).isNotBlank();
        });
        assertThat(requirementRepository.findTopSkillsBySources(List.of("TheProtocol"), org.springframework.data.domain.Pageable.unpaged()))
                .extracting("skill").contains("Java");
    }

    @Test
    void portale_sa_rozdzielone_i_nie_kasuja_sie_nawzajem() throws Exception {
        importService.importOffersFromJson(file("nofluff_offers_sample.json"), OfferSource.NOFLUFF);
        importService.importOffersFromJson(file("theprotocol_offers_sample.json"), OfferSource.THEPROTOCOL);

        assertThat(offerRepository.findBySourceOrderByFetchedAtDesc("NoFluff")).hasSize(3);
        assertThat(offerRepository.findBySourceOrderByFetchedAtDesc("TheProtocol")).hasSize(5);
        assertThat(offerRepository.countNonDuplicateBySource("NoFluff")).isEqualTo(3);
        assertThat(offerRepository.countNonDuplicateBySource("TheProtocol")).isEqualTo(5);
        assertThat(offerRepository.count()).isEqualTo(8);
    }

    @Test
    void plik_jednego_portalu_nie_moze_trafic_do_drugiego() throws Exception {
        ImportResult wrong = importService.importOffersFromJson(file("nofluff_offers_sample.json"), OfferSource.THEPROTOCOL);

        assertThat(wrong.imported()).isZero();
        assertThat(wrong.rejected()).isEqualTo(3);
        assertThat(offerRepository.count()).isZero();
    }

    @Test
    void ponowny_import_nie_tworzy_duplikatow_i_zachowuje_status_aplikowania() throws Exception {
        importService.importOffersFromJson(file("theprotocol_offers_sample.json"), OfferSource.THEPROTOCOL);
        Offer offer = offerRepository.findBySourceOrderByFetchedAtDesc("TheProtocol").get(0);
        User user = userRepository.save(User.builder().username("u").email("u@x.pl").password("p")
                .active(true).role(Role.USER).build());
        UserOfferStatus applied = new UserOfferStatus(user, offer, true);
        applied.setAppliedAt(LocalDateTime.now());
        statusRepository.save(applied);

        ImportResult again = importService.importOffersFromJson(file("theprotocol_offers_sample.json"), OfferSource.THEPROTOCOL);

        assertThat(again.imported()).isZero();
        assertThat(again.skipped()).isEqualTo(5);
        assertThat(offerRepository.count()).isEqualTo(5);
        assertThat(statusRepository.findByUser_IdAndAppliedTrue(user.getId()))
                .singleElement().extracting(s -> s.getOffer().getId()).isEqualTo(offer.getId());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED) // jak w aplikacji: import poza transakcją wywołującego
    void ponowny_import_poza_transakcja_pomija_istniejace_zamiast_bledow() throws Exception {
        try {
            importService.importOffersFromJson(file("theprotocol_offers_sample.json"), OfferSource.THEPROTOCOL);

            ImportResult again = importService.importOffersFromJson(file("theprotocol_offers_sample.json"), OfferSource.THEPROTOCOL);

            assertThat(again.errors()).isZero();
            assertThat(again.skipped()).isEqualTo(5);
        } finally {
            requirementRepository.deleteAll();
            offerRepository.deleteAll();
        }
    }

    @Test
    void import_z_json_odrzuca_oferty_innego_portalu_i_ustawia_zrodlo() {
        List<Map<String, Object>> payload = List.of(
                Map.of("offerUrl", "https://theprotocol.it/szczegoly/praca/a,oferta,01000000-0000-0000-0000-000000000001",
                        "company", "A", "title", "Java", "location", "Łódź, praca zdalna"),
                Map.of("offerUrl", "https://nofluffjobs.com/pl/job/x", "company", "B", "source", "NoFluff"),
                Map.of("offerUrl", "https://theprotocol.it/szczegoly/praca/c,oferta,01000000-0000-0000-0000-000000000003",
                        "company", "C", "source", "NoFluff"),
                Map.of("company", "bez url"));

        ImportResult result = importService.importFromPayload(payload, OfferSource.THEPROTOCOL);

        assertThat(result.imported()).isEqualTo(1);
        assertThat(result.rejected()).isEqualTo(3);
        Offer saved = offerRepository.findBySourceOrderByFetchedAtDesc("TheProtocol").get(0);
        assertThat(saved.getCity()).isEqualTo("Łódź");
        assertThat(saved.getFetchedAt()).isNotNull();
    }

    @Test
    void import_odrzuca_oferty_bez_tytulu_lub_firmy_z_parsera() {
        List<Map<String, Object>> payload = List.of(
                Map.of("offerUrl", "https://nofluffjobs.com/pl/job/a", "company", "Brak informacji o firmie", "title", "Brak tytułu", "source", "NoFluff"),
                Map.of("offerUrl", "https://nofluffjobs.com/pl/job/b", "company", "ACME", "title", "Brak tytułu", "source", "NoFluff"),
                Map.of("offerUrl", "https://nofluffjobs.com/pl/job/c", "company", "ACME", "title", "Java Developer", "source", "NoFluff"));

        ImportResult result = importService.importFromPayload(payload, OfferSource.NOFLUFF);

        assertThat(result.imported()).isEqualTo(1);
        assertThat(result.rejected()).isEqualTo(2);
        assertThat(offerRepository.findAll()).extracting(Offer::getTitle).containsExactly("Java Developer");
    }

    @Test
    void migracja_uzupelnia_brakujace_zrodlo_historycznych_ofert_nofluff() throws Exception {
        offerRepository.save(Offer.builder().title("stara").company("X").offerUrl("https://nofluffjobs.com/pl/job/stara").build());
        offerRepository.save(Offer.builder().title("obca").company("Y").offerUrl("https://example.com/job/1").build());

        backfill.run(null);

        assertThat(offerRepository.findByOfferUrl("https://nofluffjobs.com/pl/job/stara").orElseThrow().getSource()).isEqualTo("NoFluff");
        assertThat(offerRepository.findByOfferUrl("https://example.com/job/1").orElseThrow().getSource()).isNull();
    }
}
