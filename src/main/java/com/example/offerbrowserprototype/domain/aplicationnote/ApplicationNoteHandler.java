package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ApplicationNoteHandler {

    private final ApplicationNoteRepository repository;

    public ApplicationNoteHandler(ApplicationNoteRepository repository) {
        this.repository = repository;
    }

    public ApplicationNote saveApplicationNote(
            Long userId,
            Long offerId,
            String offerUrl,
            String companyName
    ) {
        if (userId == null) throw new IllegalArgumentException("User ID cannot be null");
        if (companyName == null || companyName.isBlank())
            throw new IllegalArgumentException("Company name cannot be empty");

        ApplicationNote note = ApplicationNote.builder()
                .userId(userId)
                .offerId(offerId)
                .offerUrl(offerUrl)
                .companyName(companyName)
                .appliedAt(LocalDateTime.now())
                .build();

        return repository.save(note);
    }
}

