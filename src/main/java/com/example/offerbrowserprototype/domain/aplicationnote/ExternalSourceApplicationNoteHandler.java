package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExternalSourceApplicationNoteHandler {

    private final ApplicationNoteRepository applicationNoteRepository;

    public ExternalSourceApplicationNoteHandler(ApplicationNoteRepository applicationNoteRepository) {
        this.applicationNoteRepository = applicationNoteRepository;
    }

    public ApplicationNote createNoteForExternalSource(String companyName, String url) {
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be null or empty.");
        }
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL cannot be null or empty.");
        }

        // Tworzenie nowej notatki
        ApplicationNote note = new ApplicationNote();
        note.setCompanyName(companyName);
        note.setOfferUrl(url);
        note.setAppliedAt(LocalDateTime.now()); // Ustawienie daty aplikacji

        // Zapis notatki do bazy danych
        return applicationNoteRepository.save(note);
    }
}
