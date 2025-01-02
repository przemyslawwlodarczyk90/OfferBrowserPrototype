package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationNoteGetAllHandler {

    private final ApplicationNoteRepository applicationNoteRepository;

    public ApplicationNoteGetAllHandler(ApplicationNoteRepository applicationNoteRepository) {
        this.applicationNoteRepository = applicationNoteRepository;
    }

    public List<ApplicationNote> getAllNotes() {
        return applicationNoteRepository.findAll();
    }
}
