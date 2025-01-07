package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

 class ApplicationNoteGetByCompanyNameHandlerTest {

    private ApplicationNoteRepository applicationNoteRepository;
    private ApplicationNoteGetByCompanyNameHandler applicationNoteGetByCompanyNameHandler;

    @BeforeEach
    public void setUp() {

        applicationNoteRepository = Mockito.mock(ApplicationNoteRepository.class);

        applicationNoteGetByCompanyNameHandler = new ApplicationNoteGetByCompanyNameHandler(applicationNoteRepository);
    }

    @Test
     void shouldReturnNotesForGivenCompanyName() {
        // Given
        String companyName = "Kiepscy Enterprises";
        ApplicationNote note1 = new ApplicationNote();
        note1.setId("1");
        note1.setOfferId("offer1");
        note1.setOfferUrl("https://kiepscy-jobs.com/offers/ferdynand");
        note1.setCompanyName("Kiepscy Enterprises");
        note1.setAppliedAt(LocalDateTime.now());

        ApplicationNote note2 = new ApplicationNote();
        note2.setId("2");
        note2.setOfferId("offer2");
        note2.setOfferUrl("https://kiepscy-jobs.com/offers/halinka");
        note2.setCompanyName("Kiepscy Enterprises");
        note2.setAppliedAt(LocalDateTime.now());

        List<ApplicationNote> notes = Arrays.asList(note1, note2);
        when(applicationNoteRepository.findByCompanyNameIgnoreCase(companyName)).thenReturn(notes);

        // When
        List<ApplicationNote> actualNotes = applicationNoteGetByCompanyNameHandler.getNotesByCompanyName(companyName);

        // Then
        assertThat(actualNotes).hasSize(2);
        assertThat(actualNotes).containsExactlyInAnyOrder(note1, note2);
        verify(applicationNoteRepository, times(1)).findByCompanyNameIgnoreCase(companyName.trim());
    }

    @Test
     void shouldReturnEmptyListWhenNoNotesForGivenCompanyName() {
        // Given
        String companyName = "Nonexistent Company";
        when(applicationNoteRepository.findByCompanyNameIgnoreCase(companyName)).thenReturn(Collections.emptyList());

        // When
        List<ApplicationNote> actualNotes = applicationNoteGetByCompanyNameHandler.getNotesByCompanyName(companyName);

        // Then
        assertThat(actualNotes).isEmpty();
        verify(applicationNoteRepository, times(1)).findByCompanyNameIgnoreCase(companyName.trim());
    }

    @Test
     void shouldTrimWhitespaceFromCompanyName() {
        // Given
        String companyName = "   Kiepscy Enterprises   ";
        ApplicationNote note = new ApplicationNote();
        note.setId("1");
        note.setOfferId("offer1");
        note.setOfferUrl("https://kiepscy-jobs.com/offers/ferdynand");
        note.setCompanyName("Kiepscy Enterprises");
        note.setAppliedAt(LocalDateTime.now());

        List<ApplicationNote> notes = Collections.singletonList(note);
        when(applicationNoteRepository.findByCompanyNameIgnoreCase(companyName.trim())).thenReturn(notes);

        // When
        List<ApplicationNote> actualNotes = applicationNoteGetByCompanyNameHandler.getNotesByCompanyName(companyName);

        // Then
        assertThat(actualNotes).hasSize(1);
        assertThat(actualNotes).containsExactly(note);
        verify(applicationNoteRepository, times(1)).findByCompanyNameIgnoreCase(companyName.trim());
    }

    @Test
     void shouldHandleNullCompanyName() {
        // Given
        String companyName = null;

        // When
        List<ApplicationNote> actualNotes = applicationNoteGetByCompanyNameHandler.getNotesByCompanyName(companyName);

        // Then
        assertThat(actualNotes).isEmpty();
        verify(applicationNoteRepository, times(0)).findByCompanyNameIgnoreCase(anyString());
    }
}
