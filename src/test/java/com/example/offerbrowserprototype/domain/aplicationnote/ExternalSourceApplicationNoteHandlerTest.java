package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ExternalSourceApplicationNoteHandlerTest {

    @Mock
    private ApplicationNoteRepository applicationNoteRepository;

    private ExternalSourceApplicationNoteHandler handler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new ExternalSourceApplicationNoteHandler(applicationNoteRepository);
    }

    @Test
     void shouldCreateAndSaveNoteForValidInput() {
        // Given
        String companyName = "Kiepscy Enterprises";
        String url = "https://kiepscy-jobs.com/offers/ferdynand";

        ApplicationNote expectedNote = new ApplicationNote();
        expectedNote.setCompanyName(companyName);
        expectedNote.setOfferUrl(url);
        expectedNote.setAppliedAt(LocalDateTime.now());

        when(applicationNoteRepository.save(any(ApplicationNote.class))).thenReturn(expectedNote);

        // When
        ApplicationNote result = handler.createNoteForExternalSource(companyName, url);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCompanyName()).isEqualTo(companyName);
        assertThat(result.getOfferUrl()).isEqualTo(url);
        assertThat(result.getAppliedAt()).isNotNull();

        verify(applicationNoteRepository, times(1)).save(any(ApplicationNote.class));
    }

    @Test
     void shouldThrowExceptionWhenCompanyNameIsNull() {
        // Given
        String url = "https://kiepscy-jobs.com/offers/ferdynand";

        // When / Then
        assertThatThrownBy(() -> handler.createNoteForExternalSource(null, url))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Company name cannot be null or empty.");

        verify(applicationNoteRepository, never()).save(any(ApplicationNote.class));
    }

    @Test
     void shouldThrowExceptionWhenCompanyNameIsBlank() {
        // Given
        String companyName = "   ";
        String url = "https://kiepscy-jobs.com/offers/ferdynand";

        // When / Then
        assertThatThrownBy(() -> handler.createNoteForExternalSource(companyName, url))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Company name cannot be null or empty.");

        verify(applicationNoteRepository, never()).save(any(ApplicationNote.class));
    }

    @Test
     void shouldThrowExceptionWhenUrlIsNull() {
        // Given
        String companyName = "Kiepscy Enterprises";

        // When / Then
        assertThatThrownBy(() -> handler.createNoteForExternalSource(companyName, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("URL cannot be null or empty.");

        verify(applicationNoteRepository, never()).save(any(ApplicationNote.class));
    }

    @Test
     void shouldThrowExceptionWhenUrlIsBlank() {
        // Given
        String companyName = "Kiepscy Enterprises";
        String url = "   ";

        // When / Then
        assertThatThrownBy(() -> handler.createNoteForExternalSource(companyName, url))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("URL cannot be null or empty.");

        verify(applicationNoteRepository, never()).save(any(ApplicationNote.class));
    }

    @Test
     void shouldInvokeRepositorySaveMethod() {
        // Given
        String companyName = "Paździoch Holdings";
        String url = "https://kiepscy-jobs.com/offers/pazdzioch";

        ApplicationNote savedNote = new ApplicationNote();
        savedNote.setCompanyName(companyName);
        savedNote.setOfferUrl(url);
        savedNote.setAppliedAt(LocalDateTime.now());

        when(applicationNoteRepository.save(any(ApplicationNote.class))).thenReturn(savedNote);

        // When
        handler.createNoteForExternalSource(companyName, url);

        // Then
        verify(applicationNoteRepository, times(1)).save(any(ApplicationNote.class));
    }
}
