package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.springframework.stereotype.Component;

@Component
public class ApplicationNoteCountHandler {

    private final ApplicationNoteRepository applicationNoteRepository;

    public ApplicationNoteCountHandler(ApplicationNoteRepository applicationNoteRepository) {
        this.applicationNoteRepository = applicationNoteRepository;
    }

    public long countAllNotes(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        return applicationNoteRepository.countByUserId(userId.trim());
    }
}
