package com.example.offerbrowserprototype.infrastructure.scraper;

import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService.Mode;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService.RunInfo;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService.Status;
import com.example.offerbrowserprototype.infrastructure.service.OfferImportService;
import com.example.offerbrowserprototype.infrastructure.service.OfferImportService.ImportResult;
import com.example.offerbrowserprototype.infrastructure.service.PythonProcessRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ScraperJobServiceTest {

    @TempDir Path tmp;

    private PythonProcessRunner runner;
    private OfferImportService importService;
    private ScriptLocator locator;
    private KnownOfferUrls knownUrls;
    private SearchUrlsFile searchUrls;

    private ScraperJobService service(boolean protocolEnabled) {
        ScraperSettings settings = new ScraperSettings(
                "nf.py", "nf_offer.py", tmp.resolve("nf.json").toString(), 5, true, false,
                "tp.py", "tp_offer.py", tmp.resolve("tp.json").toString(), 5, protocolEnabled, true, 60);
        return new ScraperJobService(settings, locator, runner, importService, new ObjectMapper(), knownUrls, searchUrls);
    }

    @BeforeEach
    void setUp() throws Exception {
        runner = mock(PythonProcessRunner.class);
        importService = mock(OfferImportService.class);
        locator = mock(ScriptLocator.class);
        knownUrls = mock(KnownOfferUrls.class);
        searchUrls = mock(SearchUrlsFile.class);
        when(searchUrls.writeTempFile(any())).thenAnswer(inv -> java.nio.file.Files.writeString(
                java.nio.file.Files.createTempFile(tmp, "search-", ".json"),
                "[\"https://" + ((OfferSource) inv.getArgument(0)).domain() + "/lista-z-panelu-admina\"]"));
        when(knownUrls.supports(OfferSource.NOFLUFF)).thenReturn(true);
        when(knownUrls.writeTempFile(OfferSource.NOFLUFF)).thenAnswer(inv -> java.nio.file.Files.writeString(
                tmp.resolve("known.json"), "[\"https://nofluffjobs.com/pl/job/a\"]"));
        when(locator.resolve(anyString())).thenAnswer(inv -> inv.getArgument(0));
        when(importService.importOffersFromJson(anyString(), any()))
                .thenAnswer(inv -> new ImportResult(((OfferSource) inv.getArgument(1)).value(), 5, 3, 0, 2, 0, 0));
    }

    private static PythonProcessRunner.Result ok(String summaryJson) {
        return new PythonProcessRunner.Result(0, summaryJson + "\n", "", false);
    }

    private RunInfo awaitFinished(ScraperJobService svc, OfferSource source) {
        await().atMost(5, TimeUnit.SECONDS).until(() -> !svc.isRunning(source));
        return svc.lastRun(source).orElseThrow();
    }

    @Test
    void udany_przebieg_scrapuje_i_importuje_wlasny_plik() throws Exception {
        when(runner.run(eq("tp.py"), anyList(), any(Duration.class)))
                .thenReturn(ok("{\"status\":\"SUCCESS\",\"saved\":5,\"found\":621}"));
        ScraperJobService svc = service(true);

        svc.start(OfferSource.THEPROTOCOL, Mode.SCRAPE_AND_IMPORT, "manual").orElseThrow();
        RunInfo run = awaitFinished(svc, OfferSource.THEPROTOCOL);

        assertThat(run.status()).isEqualTo(Status.SUCCESS);
        assertThat(run.scraperSummary()).containsEntry("saved", 5);
        assertThat(run.importResult()).containsEntry("imported", 3);
        verify(importService).importOffersFromJson(tmp.resolve("tp.json").toString(), OfferSource.THEPROTOCOL);
        verify(importService, never()).importOffersFromJson(anyString(), eq(OfferSource.NOFLUFF));
    }

    @Test
    void blokada_antybotowa_konczy_sie_bledem_bez_importu() throws Exception {
        when(runner.run(eq("tp.py"), anyList(), any(Duration.class))).thenReturn(new PythonProcessRunner.Result(3,
                "{\"status\":\"FAILED\",\"message\":\"Ochrona antybotowa (HTTP 403)\"}\n", "blocked", false));
        ScraperJobService svc = service(true);

        svc.start(OfferSource.THEPROTOCOL, Mode.SCRAPE_AND_IMPORT, "schedule");
        RunInfo run = awaitFinished(svc, OfferSource.THEPROTOCOL);

        assertThat(run.status()).isEqualTo(Status.FAILED);
        assertThat(run.message()).contains("antybotowa");
        verifyNoInteractions(importService);
    }

    @Test
    void timeout_procesu_jest_raportowany() {
        when(runner.run(eq("tp.py"), anyList(), any(Duration.class)))
                .thenReturn(new PythonProcessRunner.Result(-1, "", "", true));
        ScraperJobService svc = service(true);

        svc.start(OfferSource.THEPROTOCOL, Mode.SCRAPE_AND_IMPORT, "manual");

        assertThat(awaitFinished(svc, OfferSource.THEPROTOCOL).message()).contains("limit czasu");
    }

    @Test
    void ten_sam_portal_nie_startuje_drugi_raz_gdy_pracuje() throws Exception {
        CountDownLatch release = new CountDownLatch(1);
        when(runner.run(eq("tp.py"), anyList(), any(Duration.class))).thenAnswer(inv -> {
            release.await(5, TimeUnit.SECONDS);
            return ok("{\"status\":\"SUCCESS\"}");
        });
        ScraperJobService svc = service(true);

        assertThat(svc.start(OfferSource.THEPROTOCOL, Mode.SCRAPE_AND_IMPORT, "manual")).isPresent();
        assertThat(svc.start(OfferSource.THEPROTOCOL, Mode.SCRAPE_AND_IMPORT, "schedule")).isEmpty();
        assertThat(svc.isRunning(OfferSource.THEPROTOCOL)).isTrue();

        release.countDown();
        awaitFinished(svc, OfferSource.THEPROTOCOL);
        assertThat(svc.start(OfferSource.THEPROTOCOL, Mode.IMPORT_ONLY, "manual")).isPresent();
    }

    @Test
    void portale_sa_niezalezne_i_tylko_jeden_scraper_dziala_naraz() throws Exception {
        CountDownLatch nfStarted = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        when(runner.run(eq("nf.py"), anyList(), any(Duration.class))).thenAnswer(inv -> {
            nfStarted.countDown();
            release.await(5, TimeUnit.SECONDS);
            return new PythonProcessRunner.Result(1, "", "Traceback: boom", false);
        });
        when(runner.run(eq("tp.py"), anyList(), any(Duration.class))).thenReturn(ok("{\"status\":\"SUCCESS\"}"));
        ScraperJobService svc = service(true);

        svc.start(OfferSource.NOFLUFF, Mode.SCRAPE_AND_IMPORT, "manual");
        nfStarted.await(5, TimeUnit.SECONDS);
        svc.start(OfferSource.THEPROTOCOL, Mode.SCRAPE_AND_IMPORT, "manual");

        // theprotocol waits for the scraper slot instead of running a 2nd process
        Thread.sleep(200);
        assertThat(svc.lastRun(OfferSource.THEPROTOCOL).orElseThrow().status()).isEqualTo(Status.QUEUED);
        verify(runner, never()).run(eq("tp.py"), anyList(), any(Duration.class));

        release.countDown();
        assertThat(awaitFinished(svc, OfferSource.NOFLUFF).status()).isEqualTo(Status.FAILED);
        assertThat(awaitFinished(svc, OfferSource.THEPROTOCOL).status()).isEqualTo(Status.SUCCESS);
    }

    @Test
    void wylaczony_scraper_nie_startuje_ale_import_pliku_dziala() {
        ScraperJobService svc = service(false);

        assertThatThrownBy(() -> svc.start(OfferSource.THEPROTOCOL, Mode.SCRAPE_AND_IMPORT, "manual"))
                .isInstanceOf(ScraperJobService.ScraperDisabledException.class);
        assertThat(svc.start(OfferSource.THEPROTOCOL, Mode.IMPORT_ONLY, "schedule")).isPresent();
        assertThat(awaitFinished(svc, OfferSource.THEPROTOCOL).status()).isEqualTo(Status.SUCCESS);
        verifyNoInteractions(runner);
    }

    @Test
    void nofluff_dostaje_plik_z_ofertami_z_bazy_i_plik_jest_potem_usuwany() {
        java.util.concurrent.atomic.AtomicReference<java.util.List<String>> seenArgs = new java.util.concurrent.atomic.AtomicReference<>();
        when(runner.run(eq("nf.py"), anyList(), any(Duration.class))).thenAnswer(inv -> {
            java.util.List<String> args = inv.getArgument(1);
            seenArgs.set(args);
            assertThat(java.nio.file.Files.readString(java.nio.file.Path.of(args.get(3)))).contains("nofluffjobs.com/pl/job/a");
            return ok("{\"status\":\"SUCCESS\",\"new\":0,\"detailPagesOpened\":0}");
        });
        ScraperJobService svc = service(true);

        svc.start(OfferSource.NOFLUFF, Mode.SCRAPE_AND_IMPORT, "schedule");
        RunInfo run = awaitFinished(svc, OfferSource.NOFLUFF);

        assertThat(run.status()).isEqualTo(Status.SUCCESS);
        assertThat(seenArgs.get().get(2)).isEqualTo("--known-urls");
        assertThat(java.nio.file.Files.exists(java.nio.file.Path.of(seenArgs.get().get(3)))).isFalse();
    }

    @Test
    void kazdy_portal_dostaje_adresy_wyszukiwania_z_panelu_admina_a_plik_jest_potem_usuwany() throws Exception {
        java.util.concurrent.atomic.AtomicReference<java.util.List<String>> seenArgs = new java.util.concurrent.atomic.AtomicReference<>();
        when(runner.run(anyString(), anyList(), any(Duration.class))).thenAnswer(inv -> {
            java.util.List<String> args = inv.getArgument(1);
            seenArgs.set(args);
            assertThat(java.nio.file.Files.readString(java.nio.file.Path.of(args.get(1))))
                    .contains("https://justjoin.it/lista-z-panelu-admina");
            return ok("{\"status\":\"SUCCESS\",\"saved\":0}");
        });
        when(knownUrls.supports(OfferSource.JUSTJOIN)).thenReturn(true);
        when(knownUrls.writeTempFile(OfferSource.JUSTJOIN)).thenAnswer(inv -> java.nio.file.Files.writeString(
                tmp.resolve("known-jj.json"), "[]"));
        ScraperJobService svc = service(true);

        svc.start(OfferSource.JUSTJOIN, Mode.SCRAPE_AND_IMPORT, "manual");
        RunInfo run = awaitFinished(svc, OfferSource.JUSTJOIN);

        assertThat(run.status()).isEqualTo(Status.SUCCESS);
        assertThat(seenArgs.get()).hasSize(4);
        assertThat(seenArgs.get().get(0)).isEqualTo("--urls-file");
        assertThat(seenArgs.get().get(2)).isEqualTo("--known-urls");
        assertThat(java.nio.file.Files.exists(java.nio.file.Path.of(seenArgs.get().get(1)))).isFalse();
        verify(importService).importOffersFromJson("data/offers/justjoin_offers.json", OfferSource.JUSTJOIN);
    }

    @Test
    void pusta_lista_adresow_przerywa_przebieg_bez_uruchamiania_skryptu() {
        when(searchUrls.writeTempFile(OfferSource.THEPROTOCOL))
                .thenThrow(new IllegalStateException("Brak adresów wyszukiwania dla theprotocol.it — dodaj je w panelu administratora"));
        ScraperJobService svc = service(true);

        svc.start(OfferSource.THEPROTOCOL, Mode.SCRAPE_AND_IMPORT, "schedule");
        RunInfo run = awaitFinished(svc, OfferSource.THEPROTOCOL);

        assertThat(run.status()).isEqualTo(Status.FAILED);
        assertThat(run.message()).contains("Brak adresów wyszukiwania");
        verify(runner, never()).run(anyString(), anyList(), any());
        verifyNoInteractions(importService);
    }

    @Test
    void blad_odczytu_bazy_przerywa_przebieg_bez_scrapowania() {
        when(knownUrls.writeTempFile(OfferSource.NOFLUFF))
                .thenThrow(new IllegalStateException("Nie można odczytać istniejących ofert z bazy — pobieranie przerwane"));
        ScraperJobService svc = service(true);

        svc.start(OfferSource.NOFLUFF, Mode.SCRAPE_AND_IMPORT, "schedule");
        RunInfo run = awaitFinished(svc, OfferSource.NOFLUFF);

        assertThat(run.status()).isEqualTo(Status.FAILED);
        assertThat(run.message()).contains("pobieranie przerwane");
        verify(runner, never()).run(eq("nf.py"), anyList(), any());
        verifyNoInteractions(importService);
    }

    @Test
    void czesciowy_wynik_scrapera_daje_status_partial() {
        when(runner.run(eq("tp.py"), anyList(), any(Duration.class))).thenReturn(ok("{\"status\":\"PARTIAL\",\"errors\":2}"));
        ScraperJobService svc = service(true);

        svc.start(OfferSource.THEPROTOCOL, Mode.SCRAPE_AND_IMPORT, "manual");

        assertThat(awaitFinished(svc, OfferSource.THEPROTOCOL).status()).isEqualTo(Status.PARTIAL);
    }
}
