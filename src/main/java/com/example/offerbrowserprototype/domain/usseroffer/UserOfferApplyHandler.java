package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNoteHandler;
import com.example.offerbrowserprototype.domain.exception.OfferAlreadyAppliedException;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserOfferApplyHandler {

    private final UserOfferStatusRepository userOfferStatusRepository;
    private final OfferRepository offerRepository; // Repozytorium ofert, aby pobrać szczegóły oferty
    private final ApplicationNoteHandler applicationNoteHandler; // Handler do zapisywania notatek aplikacyjnych

    public UserOfferApplyHandler(UserOfferStatusRepository userOfferStatusRepository,
                                 OfferRepository offerRepository,
                                 ApplicationNoteHandler applicationNoteHandler) {
        this.userOfferStatusRepository = userOfferStatusRepository;
        this.offerRepository = offerRepository;
        this.applicationNoteHandler = applicationNoteHandler;
    }

    public UserOfferStatus applyToOffer(String userId, String offerId) {
        UserOfferStatus userOfferStatus = userOfferStatusRepository
                .findByUserIdAndOfferId(userId, offerId)
                .orElse(new UserOfferStatus(userId, offerId, false));

        if (userOfferStatus.isApplied()) {
            throw new OfferAlreadyAppliedException("Offer already applied for userId: " + userId);
        }

        // Zaznacz ofertę jako zaaplikowaną
        userOfferStatus.setApplied(true);
        userOfferStatus.setAppliedAt(LocalDateTime.now());
        userOfferStatusRepository.save(userOfferStatus);

        // Pobierz szczegóły oferty
        var offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found for ID: " + offerId));

        // Utwórz notatkę aplikacyjną
        applicationNoteHandler.saveApplicationNote(
                userId,
                offerId,
                offer.getOfferUrl(),
                offer.getCompany()
        );

        return userOfferStatus;
    }
}
