//package com.example.offerbrowserprototype.domain.aplicationnote;
//
//import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//class ApplicationNoteGetCompaniesWithDatesHandlerTest {
//
//    @Mock
//    private ApplicationNoteRepository applicationNoteRepository;
//
//    private ApplicationNoteGetCompaniesWithDatesHandler handler;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        handler = new ApplicationNoteGetCompaniesWithDatesHandler(applicationNoteRepository);
//    }
//
//    @Test
//     void shouldReturnEmptyMapWhenNoNotesExist() {
//        // Given
//        when(applicationNoteRepository.findAll()).thenReturn(Collections.emptyList());
//
//        // When
//        Map<String, List<String>> result = handler.getCompaniesWithApplicationDates();
//
//        // Then
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//     void shouldGroupCompaniesWithSortedApplicationDates() {
//        // Given
//        ApplicationNote note1 = new ApplicationNote();
//        note1.setCompanyName("Kiepscy Enterprises");
//        note1.setAppliedAt(LocalDateTime.of(2025, 1, 1, 12, 0));
//
//        ApplicationNote note2 = new ApplicationNote();
//        note2.setCompanyName("Kiepscy Enterprises");
//        note2.setAppliedAt(LocalDateTime.of(2025, 1, 2, 12, 0));
//
//        ApplicationNote note3 = new ApplicationNote();
//        note3.setCompanyName("Paździoch Holdings");
//        note3.setAppliedAt(LocalDateTime.of(2025, 1, 3, 12, 0));
//
//        when(applicationNoteRepository.findAll()).thenReturn(Arrays.asList(note1, note2, note3));
//
//        // When
//        Map<String, List<String>> result = handler.getCompaniesWithApplicationDates();
//
//        // Then
//        assertThat(result).hasSize(2);
//        assertThat(result.get("Kiepscy Enterprises")).containsExactly("2025-01-02", "2025-01-01");
//        assertThat(result.get("Paździoch Holdings")).containsExactly("2025-01-03");
//    }
//
//    @Test
//     void shouldIgnoreNotesWithNullAppliedAt() {
//        // Given
//        ApplicationNote note1 = new ApplicationNote();
//        note1.setCompanyName("Kiepscy Enterprises");
//        note1.setAppliedAt(LocalDateTime.of(2025, 1, 1, 12, 0));
//
//        ApplicationNote note2 = new ApplicationNote();
//        note2.setCompanyName("Kiepscy Enterprises");
//        note2.setAppliedAt(null);
//
//        when(applicationNoteRepository.findAll()).thenReturn(Arrays.asList(note1, note2));
//
//        // When
//        Map<String, List<String>> result = handler.getCompaniesWithApplicationDates();
//
//        // Then
//        assertThat(result).hasSize(1);
//        assertThat(result.get("Kiepscy Enterprises")).containsExactly("2025-01-01");
//    }
//
//    @Test
//     void shouldReturnEmptyMapWhenAllNotesHaveNullAppliedAt() {
//        // Given
//        ApplicationNote note1 = new ApplicationNote();
//        note1.setCompanyName("Kiepscy Enterprises");
//        note1.setAppliedAt(null);
//
//        ApplicationNote note2 = new ApplicationNote();
//        note2.setCompanyName("Paździoch Holdings");
//        note2.setAppliedAt(null);
//
//        when(applicationNoteRepository.findAll()).thenReturn(Arrays.asList(note1, note2));
//
//        // When
//        Map<String, List<String>> result = handler.getCompaniesWithApplicationDates();
//
//        // Then
//        assertThat(result).isEmpty();
//    }
//}
