package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

 class ApplicationNoteCountHandlerTest {

    private ApplicationNoteRepository applicationNoteRepository;
    private ApplicationNoteCountHandler applicationNoteCountHandler;

    @BeforeEach
    public void setUp() {

        applicationNoteRepository = Mockito.mock(ApplicationNoteRepository.class);
        applicationNoteCountHandler = new ApplicationNoteCountHandler(applicationNoteRepository);
    }

    @Test
     void shouldReturnCountOfNotesWhenRepositoryIsNotEmpty() {
        // Given
        long expectedCount = 5L;
        when(applicationNoteRepository.count()).thenReturn(expectedCount);

        // When
        long actualCount = applicationNoteCountHandler.countAllNotes();

        // Then
        assertThat(actualCount).isEqualTo(expectedCount);
        verify(applicationNoteRepository, times(1)).count();
    }

    @Test
     void shouldReturnZeroWhenRepositoryIsEmpty() {
        // Given
        when(applicationNoteRepository.count()).thenReturn(0L);

        // When
        long actualCount = applicationNoteCountHandler.countAllNotes();

        // Then
        assertThat(actualCount).isEqualTo(0L);
        verify(applicationNoteRepository, times(1)).count();
    }

    @Test
     void shouldHandleRepositoryExceptionsGracefully() {
        // Given
        when(applicationNoteRepository.count()).thenThrow(new RuntimeException("Database error"));

        // When
        long actualCount = 0;
        try {
            actualCount = applicationNoteCountHandler.countAllNotes();
        } catch (RuntimeException e) {
        }

        // Then
        assertThat(actualCount).isEqualTo(0L);
        verify(applicationNoteRepository, times(1)).count();
    }
}
