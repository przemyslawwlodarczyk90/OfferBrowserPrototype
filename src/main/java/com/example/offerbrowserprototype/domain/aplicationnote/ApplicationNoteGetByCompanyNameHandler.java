package com.example.offerbrowserprototype.domain.aplicationnote;


import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationNoteGetByCompanyNameHandler {

    private final ApplicationNoteRepository applicationNoteRepository;

    public ApplicationNoteGetByCompanyNameHandler(ApplicationNoteRepository applicationNoteRepository) {
        this.applicationNoteRepository = applicationNoteRepository;
    }

    public List<ApplicationNote> getNotesByCompanyName(String companyName) {
        // Wywołanie metody repozytorium z ignorowaniem wielkości liter
        return applicationNoteRepository.findByCompanyNameIgnoreCase(companyName.trim());
    }
}
