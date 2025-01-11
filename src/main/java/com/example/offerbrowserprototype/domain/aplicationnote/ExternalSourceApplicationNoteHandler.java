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

    public ApplicationNote createNoteForExternalSource(String userId, String companyName, String url) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be null or empty.");
        }
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL cannot be null or empty.");
        }

        ApplicationNote note = new ApplicationNote();
        note.setUserId(userId);
        note.setCompanyName(companyName);
        note.setOfferUrl(url);
        note.setAppliedAt(LocalDateTime.now());

        return applicationNoteRepository.save(note);
    }
}
