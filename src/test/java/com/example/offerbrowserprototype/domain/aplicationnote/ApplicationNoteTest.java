package com.example.offerbrowserprototype.domain.aplicationnote;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationNoteTest {

    @Test
    public void shouldSetAndGetPropertiesForFerdynandKiepski() {
        // Dane Ferdynanda Kiepskiego
        String expectedId = "1";
        String expectedOfferId = "offer001";
        String expectedOfferUrl = "https://kiepscy-jobs.com/offers/ferdynand";
        String expectedCompanyName = "Kiepscy Enterprises";
        LocalDateTime expectedAppliedAt = LocalDateTime.now();

        // When
        ApplicationNote applicationNote = new ApplicationNote();
        applicationNote.setId(expectedId);
        applicationNote.setOfferId(expectedOfferId);
        applicationNote.setOfferUrl(expectedOfferUrl);
        applicationNote.setCompanyName(expectedCompanyName);
        applicationNote.setAppliedAt(expectedAppliedAt);

        // Then
        assertThat(applicationNote.getId()).isEqualTo(expectedId);
        assertThat(applicationNote.getOfferId()).isEqualTo(expectedOfferId);
        assertThat(applicationNote.getOfferUrl()).isEqualTo(expectedOfferUrl);
        assertThat(applicationNote.getCompanyName()).isEqualTo(expectedCompanyName);
        assertThat(applicationNote.getAppliedAt()).isEqualTo(expectedAppliedAt);
    }

    @Test
    public void shouldSetAndGetPropertiesForMarianPazdzioch() {
        // Dane Mariana Paździocha
        String expectedId = "2";
        String expectedOfferId = "offer002";
        String expectedOfferUrl = "https://kiepscy-jobs.com/offers/pazdzioch";
        String expectedCompanyName = "Paździoch Holdings";
        LocalDateTime expectedAppliedAt = LocalDateTime.now();

        // When
        ApplicationNote applicationNote = new ApplicationNote();
        applicationNote.setId(expectedId);
        applicationNote.setOfferId(expectedOfferId);
        applicationNote.setOfferUrl(expectedOfferUrl);
        applicationNote.setCompanyName(expectedCompanyName);
        applicationNote.setAppliedAt(expectedAppliedAt);

        // Then
        assertThat(applicationNote.getId()).isEqualTo(expectedId);
        assertThat(applicationNote.getOfferId()).isEqualTo(expectedOfferId);
        assertThat(applicationNote.getOfferUrl()).isEqualTo(expectedOfferUrl);
        assertThat(applicationNote.getCompanyName()).isEqualTo(expectedCompanyName);
        assertThat(applicationNote.getAppliedAt()).isEqualTo(expectedAppliedAt);
    }

    @Test
    public void shouldVerifyEqualityForHelenaKopciowa() {
        // Dane Heleny Kopciowej
        ApplicationNote note1 = new ApplicationNote();
        note1.setId("3");
        note1.setOfferId("offer003");
        note1.setOfferUrl("https://kiepscy-jobs.com/offers/kopciowa");
        note1.setCompanyName("Helena's Cleaning Services");
        note1.setAppliedAt(LocalDateTime.now());

        ApplicationNote note2 = new ApplicationNote();
        note2.setId("3");
        note2.setOfferId("offer003");
        note2.setOfferUrl("https://kiepscy-jobs.com/offers/kopciowa");
        note2.setCompanyName("Helena's Cleaning Services");
        note2.setAppliedAt(note1.getAppliedAt());

        // Weryfikacja równości
        assertThat(note1).isEqualTo(note2);
        assertThat(note1.hashCode()).isEqualTo(note2.hashCode());
    }

    @Test
    public void shouldNotEqualDifferentNotes() {
        // Dane różnych postaci
        ApplicationNote note1 = new ApplicationNote();
        note1.setId("4");
        note1.setOfferId("offer004");
        note1.setOfferUrl("https://kiepscy-jobs.com/offers/boczek");
        note1.setCompanyName("Boczek's Meat Supplies");
        note1.setAppliedAt(LocalDateTime.now());

        ApplicationNote note2 = new ApplicationNote();
        note2.setId("5");
        note2.setOfferId("offer005");
        note2.setOfferUrl("https://kiepscy-jobs.com/offers/maliniski");
        note2.setCompanyName("Malinowski Logistics");
        note2.setAppliedAt(LocalDateTime.now());

        // Weryfikacja nierówności
        assertThat(note1).isNotEqualTo(note2);
    }
}
