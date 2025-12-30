package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ApplicationNoteHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationNoteHandler.class);

    private final ApplicationNoteRepository applicationNoteRepository;

    public ApplicationNoteHandler(ApplicationNoteRepository applicationNoteRepository) {
        this.applicationNoteRepository = applicationNoteRepository;
    }

    public ApplicationNote saveApplicationNote(String userId, String offerId, String offerUrl, String companyName) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (offerUrl == null || offerUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Offer URL cannot be null or empty.");
        }
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be null or empty.");
        }

        logger.info("Saving application note for user: {}, offerId: {}, offerUrl: {}, company: {}",
                userId, offerId, offerUrl, companyName);

        ApplicationNote note = new ApplicationNote();
        note.setUserId(userId.trim());
        note.setOfferId(offerId);
        note.setOfferUrl(offerUrl.trim());
        note.setCompanyName(companyName.trim());
        note.setAppliedAt(LocalDateTime.now());

        return applicationNoteRepository.save(note);
    }
}
