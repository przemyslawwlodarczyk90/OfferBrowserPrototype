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

public class ApplicationNoteGetAllHandlerTest {

    private ApplicationNoteRepository applicationNoteRepository;
    private ApplicationNoteGetAllHandler applicationNoteGetAllHandler;

    @BeforeEach
    public void setUp() {
        // Mockowanie repozytorium
        applicationNoteRepository = Mockito.mock(ApplicationNoteRepository.class);
        // Inicjalizacja klasy testowanej
        applicationNoteGetAllHandler = new ApplicationNoteGetAllHandler(applicationNoteRepository);
    }

    @Test
    public void shouldReturnAllNotesWhenRepositoryIsNotEmpty() {
        // Given
        ApplicationNote note1 = new ApplicationNote();
        note1.setId("1");
        note1.setOfferId("offer1");
        note1.setOfferUrl("https://kiepscy-jobs.com/offers/ferdynand");
        note1.setCompanyName("Kiepscy Enterprises");
        note1.setAppliedAt(LocalDateTime.now());

        ApplicationNote note2 = new ApplicationNote();
        note2.setId("2");
        note2.setOfferId("offer2");
        note2.setOfferUrl("https://kiepscy-jobs.com/offers/pazdzioch");
        note2.setCompanyName("Paździoch Holdings");
        note2.setAppliedAt(LocalDateTime.now());

        List<ApplicationNote> notes = Arrays.asList(note1, note2);
        when(applicationNoteRepository.findAll()).thenReturn(notes);

        // When
        List<ApplicationNote> actualNotes = applicationNoteGetAllHandler.getAllNotes();

        // Then
        assertThat(actualNotes).hasSize(2);
        assertThat(actualNotes).containsExactlyInAnyOrder(note1, note2);
        verify(applicationNoteRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnEmptyListWhenRepositoryIsEmpty() {
        // Given
        when(applicationNoteRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<ApplicationNote> actualNotes = applicationNoteGetAllHandler.getAllNotes();

        // Then
        assertThat(actualNotes).isEmpty();
        verify(applicationNoteRepository, times(1)).findAll();
    }

    @Test
    public void shouldHandleRepositoryExceptionsGracefully() {
        // Given
        when(applicationNoteRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When
        List<ApplicationNote> actualNotes = Collections.emptyList();
        try {
            actualNotes = applicationNoteGetAllHandler.getAllNotes();
        } catch (RuntimeException e) {
            // Expected exception, no action required
        }

        // Then
        assertThat(actualNotes).isEmpty();
        verify(applicationNoteRepository, times(1)).findAll();
    }
}
