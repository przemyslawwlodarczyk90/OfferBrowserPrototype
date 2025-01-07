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


    @Test
     void shouldSaveAndRetrieveApplicationNote() {

        ApplicationNote note = new ApplicationNote();
        note.setOfferId("offer123");
        note.setOfferUrl("https://kiepscy-jobs.com/offers/ferdynand");
        note.setCompanyName("Kiepscy Enterprises");
        note.setAppliedAt(LocalDateTime.now());

        applicationNoteRepository.save(note);


        Optional<ApplicationNote> retrievedNote = applicationNoteRepository.findByOfferUrl("https://kiepscy-jobs.com/offers/ferdynand");

        assertThat(retrievedNote).isPresent();
        assertThat(retrievedNote.get().getCompanyName()).isEqualTo("Kiepscy Enterprises");
    }


    @Test
     void shouldNotFindNonExistentApplicationNote() {

        Optional<ApplicationNote> retrievedNote = applicationNoteRepository.findByOfferUrl("https://nieistniejacy-url.com");


        assertThat(retrievedNote).isNotPresent();
    }


}
