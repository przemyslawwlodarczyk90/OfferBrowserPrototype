package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class ExternalSourceApplicationNoteHandler {

    private final ApplicationNoteRepository repository;

    public ExternalSourceApplicationNoteHandler(ApplicationNoteRepository repository) {
        this.repository = repository;
    }

    public ApplicationNote createNoteForExternalSource(
            Long userId,
            String companyName,
            String offerUrl
    ) {
        if (userId == null) throw new IllegalArgumentException("User ID cannot be null");

        ApplicationNote note = ApplicationNote.builder()
                .userId(userId)
                .companyName(companyName)
                .offerUrl(offerUrl)
                .appliedAt(LocalDateTime.now())
                .build();

        return repository.save(note);
    }
}
