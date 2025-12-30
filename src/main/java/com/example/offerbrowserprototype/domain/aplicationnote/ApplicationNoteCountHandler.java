package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.springframework.stereotype.Component;

@Component
public class ApplicationNoteCountHandler {

    private final ApplicationNoteRepository applicationNoteRepository;

    public ApplicationNoteCountHandler(ApplicationNoteRepository applicationNoteRepository) {
        this.applicationNoteRepository = applicationNoteRepository;
    }

    public long countAllNotes(Long userId) {
        return applicationNoteRepository.countByUserId(userId);
    }

}
