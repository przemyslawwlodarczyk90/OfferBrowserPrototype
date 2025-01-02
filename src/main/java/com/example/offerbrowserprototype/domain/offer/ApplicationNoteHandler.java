package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
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

    public void saveApplicationNote(String offerId, String offerUrl, String companyName) {
        logger.info("Saving application note for offer: {} (URL: {}, Company: {})", offerId, offerUrl, companyName);
        ApplicationNote note = new ApplicationNote();
        note.setOfferId(offerId);
        note.setOfferUrl(offerUrl);
        note.setCompanyName(companyName);
        note.setAppliedAt(LocalDateTime.now());
        applicationNoteRepository.save(note);
    }
}
