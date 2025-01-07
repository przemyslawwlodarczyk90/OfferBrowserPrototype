package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ApplicationNoteRepositoryIntegrationTest {

    @Autowired
    private ApplicationNoteRepository applicationNoteRepository;

    @BeforeEach
    public void setUp() {
        applicationNoteRepository.deleteAll();
    }

    /**
     * Test sprawdzający zapis i odczyt notatki aplikacyjnej z bazy.
     */
    @Test
    public void shouldSaveAndRetrieveApplicationNote() {
        // Dane testowe: Ferdynand Kiepski
        ApplicationNote note = new ApplicationNote();
        note.setOfferId("offer123");
        note.setOfferUrl("https://kiepscy-jobs.com/offers/ferdynand");
        note.setCompanyName("Kiepscy Enterprises");
        note.setAppliedAt(LocalDateTime.now());

        // Zapis notatki do bazy
        applicationNoteRepository.save(note);

        // Pobranie notatki z bazy
        Optional<ApplicationNote> retrievedNote = applicationNoteRepository.findByOfferUrl("https://kiepscy-jobs.com/offers/ferdynand");

        // Weryfikacja
        assertThat(retrievedNote).isPresent();
        assertThat(retrievedNote.get().getCompanyName()).isEqualTo("Kiepscy Enterprises");
    }

    /**
     * Test sprawdzający brak notatki w bazie.
     */
    @Test
    public void shouldNotFindNonExistentApplicationNote() {
        // Próba wyszukania notatki, która nie istnieje
        Optional<ApplicationNote> retrievedNote = applicationNoteRepository.findByOfferUrl("https://nieistniejacy-url.com");

        // Weryfikacja, że notatka nie istnieje
        assertThat(retrievedNote).isNotPresent();
    }


}
