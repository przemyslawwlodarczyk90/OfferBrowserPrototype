package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ApplicationNoteGetByCompanyNameHandler {

    private final ApplicationNoteRepository applicationNoteRepository;

    public ApplicationNoteGetByCompanyNameHandler(ApplicationNoteRepository applicationNoteRepository) {
        this.applicationNoteRepository = applicationNoteRepository;
    }

    public List<ApplicationNote> getNotesByCompanyName(String userId, String companyName) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (companyName == null || companyName.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return applicationNoteRepository.findByUserIdAndCompanyNameIgnoreCase(userId.trim(), companyName.trim());
    }
}
