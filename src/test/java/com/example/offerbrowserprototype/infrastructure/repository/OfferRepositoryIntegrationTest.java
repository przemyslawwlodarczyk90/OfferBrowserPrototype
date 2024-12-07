package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.offer.Offer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OfferRepositoryIntegrationTest {

    @Autowired
    private OfferRepository offerRepository;

    private Clock fixedClock1;
    private Clock fixedClock2;

    @BeforeEach
    public void setUp() {
        offerRepository.deleteAll(); // Czyszczenie bazy przed każdym testem

        // Przygotowanie stałych zegarów na potrzeby testów
        fixedClock1 = Clock.fixed(Instant.parse("2024-12-05T12:00:00Z"), ZoneId.of("UTC"));
        fixedClock2 = Clock.fixed(Instant.parse("2024-12-06T10:00:00Z"), ZoneId.of("UTC"));
    }

    /**
     * Test sprawdzający pobranie wszystkich ofert posortowanych według daty.
     */
    @Test
    public void shouldFindAllOffersOrderedByFetchedAtDesc() {
        // Dane testowe
        Offer offer1 = new Offer("Programista Java", "Praca w zespole", "Wrocław", "5000-7000 PLN", "Java, Spring", fixedClock1);
        Offer offer2 = new Offer("Sprzątacz", "Sprzątanie klatek", "Warszawa", "2500 PLN", "Brak", fixedClock2);

        // Zapis ofert do bazy
        offerRepository.save(offer1);
        offerRepository.save(offer2);

        // Pobranie ofert z bazy
        List<Offer> offers = offerRepository.findAllByOrderByFetchedAtDesc();

        // Weryfikacja kolejności
        assertThat(offers).hasSize(2);
        assertThat(offers.get(0).getTitle()).isEqualTo("Sprzątacz");
        assertThat(offers.get(1).getTitle()).isEqualTo("Programista Java");
    }

    /**
     * Test sprawdzający pobranie ofert, na które nie aplikowano.
     */
    @Test
    public void shouldFindNotAppliedOffersOrderedByFetchedAtDesc() {
        // Dane testowe
        Offer offer1 = new Offer("Ochroniarz", "Praca na nocną zmianę", "Poznań", "3000-4000 PLN", "Siła fizyczna", fixedClock1);
        Offer offer2 = new Offer("Programista C++", "Praca nad grami", "Gdańsk", "6000-8000 PLN", "C++, Unreal", fixedClock2);
        offer2.setApplied(true); // Oznaczenie oferty jako aplikowanej

        // Zapis ofert do bazy
        offerRepository.save(offer1);
        offerRepository.save(offer2);

        // Pobranie ofert, na które nie aplikowano
        List<Offer> notAppliedOffers = offerRepository.findByAppliedFalseOrderByFetchedAtDesc();

        // Weryfikacja
        assertThat(notAppliedOffers).hasSize(1);
        assertThat(notAppliedOffers.get(0).getTitle()).isEqualTo("Ochroniarz");
    }

    /**
     * Test sprawdzający pobranie ofert, na które aplikowano.
     */
    @Test
    public void shouldFindAppliedOffersOrderedByFetchedAtDesc() {
        // Dane testowe
        Offer offer1 = new Offer("Kucharka", "Gotowanie obiadów", "Lublin", "4000 PLN", "Kuchnia polska", fixedClock1);
        offer1.setApplied(true); // Oznaczenie oferty jako aplikowanej
        Offer offer2 = new Offer("Sekretarka", "Praca biurowa", "Kraków", "3500 PLN", "MS Office", fixedClock2);

        // Zapis ofert do bazy
        offerRepository.save(offer1);
        offerRepository.save(offer2);

        // Pobranie ofert, na które aplikowano
        List<Offer> appliedOffers = offerRepository.findByAppliedTrueOrderByFetchedAtDesc();

        // Weryfikacja
        assertThat(appliedOffers).hasSize(1);
        assertThat(appliedOffers.get(0).getTitle()).isEqualTo("Kucharka");
    }
}
