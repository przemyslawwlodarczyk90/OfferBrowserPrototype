//package com.example.offerbrowserprototype.domain.offer;
//
//import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
//import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNoteHandler;
//import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//class ApplicationNoteHandlerTest {
//
//    @Mock
//    private ApplicationNoteRepository applicationNoteRepository;
//
//    private ApplicationNoteHandler applicationNoteHandler;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        applicationNoteHandler = new ApplicationNoteHandler(applicationNoteRepository);
//    }
//
//    @Test
//    void shouldSaveApplicationNote() {
//        // Given
//        String offerId = "offer123";
//        String offerUrl = "https://example.com/offers/123";
//        String companyName = "Example Company";
//
//        // When
//        applicationNoteHandler.saveApplicationNote(offerId, offerUrl, companyName);
//
//        // Then
//        ArgumentCaptor<ApplicationNote> captor = ArgumentCaptor.forClass(ApplicationNote.class);
//        verify(applicationNoteRepository, times(1)).save(captor.capture());
//
//        ApplicationNote savedNote = captor.getValue();
//        assertThat(savedNote.getOfferId()).isEqualTo(offerId);
//        assertThat(savedNote.getOfferUrl()).isEqualTo(offerUrl);
//        assertThat(savedNote.getCompanyName()).isEqualTo(companyName);
//        assertThat(savedNote.getAppliedAt()).isNotNull();
//        assertThat(savedNote.getAppliedAt()).isBeforeOrEqualTo(LocalDateTime.now());
//    }
//
//    @Test
//    void shouldLogSavingApplicationNote() {
//        // Given
//        String offerId = "offer456";
//        String offerUrl = "https://example.com/offers/456";
//        String companyName = "Another Company";
//
//        // When
//        applicationNoteHandler.saveApplicationNote(offerId, offerUrl, companyName);
//
//        // Then
//        verify(applicationNoteRepository, times(1)).save(any(ApplicationNote.class));
//        // Ensure the method logs the correct message (optional, if you are testing logger output in a real environment)
//    }
//
//    @Test
//    void shouldNotSaveWhenRepositoryThrowsException() {
//        // Given
//        String offerId = "offer789";
//        String offerUrl = "https://example.com/offers/789";
//        String companyName = "Company with Error";
//
//        doThrow(new RuntimeException("Database error")).when(applicationNoteRepository).save(any(ApplicationNote.class));
//
//        // When
//        try {
//            applicationNoteHandler.saveApplicationNote(offerId, offerUrl, companyName);
//        } catch (RuntimeException e) {
//            // Expected exception
//        }
//
//        // Then
//        verify(applicationNoteRepository, times(1)).save(any(ApplicationNote.class));
//    }
//}
