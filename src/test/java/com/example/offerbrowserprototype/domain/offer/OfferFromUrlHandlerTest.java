package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNoteHandler;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.domain.requirement.NiceToHaveCreationHandler;
import com.example.offerbrowserprototype.domain.requirement.RequirementCreationHandler;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperSettings;
import com.example.offerbrowserprototype.infrastructure.scraper.ScriptLocator;
import com.example.offerbrowserprototype.infrastructure.service.PythonProcessRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OfferFromUrlHandlerTest {

    private static final String TP_URL =
            "https://theprotocol.it/szczegoly/praca/java-dev-lodz,oferta,01000000-183e-46c7-5d79-08df154cbe3d";

    private OfferRepository offerRepository;
    private PythonProcessRunner runner;
    private OfferFromUrlHandler handler;

    private OfferFromUrlHandler handler(boolean protocolEnabled) {
        ScraperSettings settings = new ScraperSettings("nf.py", "nf_offer.py", "nf.json", 5, true, false,
                "tp.py", "tp_offer.py", "tp.json", 5, protocolEnabled, true, 60);
        ScriptLocator locator = mock(ScriptLocator.class);
        when(locator.resolve(anyString())).thenAnswer(inv -> inv.getArgument(0));
        UserRepository users = mock(UserRepository.class);
        when(users.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).username("u").build()));
        return new OfferFromUrlHandler(offerRepository, new OfferMapper(), mock(ApplicationNoteHandler.class),
                mock(UserOfferStatusRepository.class), users, mock(RequirementCreationHandler.class),
                mock(NiceToHaveCreationHandler.class), runner, settings, locator);
    }

    @BeforeEach
    void setUp() {
        offerRepository = mock(OfferRepository.class);
        runner = mock(PythonProcessRunner.class);
        when(offerRepository.save(any(Offer.class))).thenAnswer(inv -> {
            Offer o = inv.getArgument(0);
            o.setId(10L);
            return o;
        });
        handler = handler(true);
    }

    @Test
    void url_theprotocol_uruchamia_scraper_theprotocol_i_zapisuje_zrodlo() {
        when(runner.run(eq("tp_offer.py"), eq(List.of(TP_URL)), any(Duration.class))).thenReturn(new PythonProcessRunner.Result(0,
                "{\"title\":\"Java Dev\",\"description\":\"d\",\"location\":\"Łódź\",\"offerUrl\":\"" + TP_URL
                        + "\",\"company\":\"ACME\",\"level\":\"Junior\",\"fetchedAt\":\"2026-10-11T10:00:00.123Z\","
                        + "\"requirements\":[\"Java\"],\"niceToHave\":[],\"source\":\"TheProtocol\",\"salaryRange\":\"Brak wynagrodzenia\"}\n",
                "", false));

        var dto = handler.addOfferFromUrl(1L, TP_URL);

        assertThat(dto.getSource()).isEqualTo("TheProtocol");
        assertThat(dto.getCity()).isEqualTo("Łódź");
        verify(runner, never()).run(eq("nf_offer.py"), anyList(), any());
    }

    @Test
    void nieznana_domena_jest_odrzucana_bez_uruchamiania_skryptu() {
        assertThatThrownBy(() -> handler.addOfferFromUrl(1L, "http://169.254.169.254/latest/meta-data"))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(runner);
    }

    @Test
    void istniejaca_oferta_daje_konflikt_bez_scrapowania() {
        when(offerRepository.existsByOfferUrl(TP_URL)).thenReturn(true);

        assertThatThrownBy(() -> handler.addOfferFromUrl(1L, TP_URL)).isInstanceOf(IllegalStateException.class);
        verifyNoInteractions(runner);
    }

    @Test
    void blad_scrapera_ma_czytelny_komunikat() {
        when(runner.run(anyString(), anyList(), any())).thenReturn(new PythonProcessRunner.Result(4, "", "not found", false));

        assertThatThrownBy(() -> handler.addOfferFromUrl(1L, TP_URL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nie istnieje");
    }

    @Test
    void pusta_oferta_ze_strony_bledu_nie_jest_zapisywana() {
        // dokładny wynik niezmienionego skryptu NoFluff (HEAD) dla strony CloudFront 403 — kod 0, same teksty zastępcze
        String nofluffUrl = "https://nofluffjobs.com/pl/job/solution-architect-scalo-warszawa-9";
        when(runner.run(eq("nf_offer.py"), eq(List.of(nofluffUrl)), any(Duration.class)))
                .thenReturn(new PythonProcessRunner.Result(0, "{\"title\": \"Brak tytu\u0142u\", \"description\": \"Brak opisu\", \"location\": \"Nieokre\u015blona\", \"salaryRange\": \"Brak wynagrodzenia\", \"level\": \"Nieokre\u015blony poziom\", \"fetchedAt\": \"2026-10-11T11:57:02.229Z\", \"offerUrl\": \"https://nofluffjobs.com/pl/job/solution-architect-scalo-warszawa-9\", \"company\": \"Brak informacji o firmie\", \"requirements\": [], \"source\": \"NoFluff\"}", "", false));

        assertThatThrownBy(() -> handler.addOfferFromUrl(1L, nofluffUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nie zapisano oferty");
        verify(offerRepository, never()).save(any());
    }

    @Test
    void wylaczony_portal_nie_jest_pobierany() {
        handler = handler(false);

        assertThatThrownBy(() -> handler.addOfferFromUrl(1L, TP_URL))
                .isInstanceOf(ScraperJobService.ScraperDisabledException.class);
        verifyNoInteractions(runner);
    }
}
