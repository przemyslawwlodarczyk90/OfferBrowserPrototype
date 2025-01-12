package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class ApplicationNoteHandler {
    private final ApplicationNoteRepository applicationNoteRepository;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationNoteHandler.class);

    public ApplicationNoteHandler(ApplicationNoteRepository applicationNoteRepository) {
        this.applicationNoteRepository = applicationNoteRepository;
    }

    public void saveApplicationNote(String userId, String offerId, String offerUrl, String companyName) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (offerUrl == null || offerUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Offer URL cannot be null or empty.");
        }
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be null or empty.");
        }

        logger.info("Saving application note for user: {}, offer: {} (URL: {}, Company: {})", userId, offerId, offerUrl, companyName);

        ApplicationNote note = new ApplicationNote();
        note.setUserId(userId);
        note.setOfferId(offerId);
        note.setOfferUrl(offerUrl);
        note.setCompanyName(companyName);
        note.setAppliedAt(LocalDateTime.now());

        applicationNoteRepository.save(note);
    }
}
