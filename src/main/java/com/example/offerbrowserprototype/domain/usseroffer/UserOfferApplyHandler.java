package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNoteHandler;
import com.example.offerbrowserprototype.domain.exception.OfferAlreadyAppliedException;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UserOfferApplyHandler {

    private final UserOfferStatusRepository userOfferStatusRepository;
    private final OfferRepository offerRepository;
    private final ApplicationNoteHandler applicationNoteHandler;

    public UserOfferApplyHandler(UserOfferStatusRepository userOfferStatusRepository,
                                 OfferRepository offerRepository,
                                 ApplicationNoteHandler applicationNoteHandler) {
        this.userOfferStatusRepository = userOfferStatusRepository;
        this.offerRepository = offerRepository;
        this.applicationNoteHandler = applicationNoteHandler;
    }

    @Transactional
    public UserOfferStatus applyToOffer(String userId, String offerId) {

        UserOfferStatus status = userOfferStatusRepository
                .findByUserIdAndOfferId(userId, offerId)
                .orElse(new UserOfferStatus(userId, offerId, false));

        if (status.isApplied()) {
            throw new OfferAlreadyAppliedException(
                    "Offer already applied for userId: " + userId
            );
        }

        status.setApplied(true);
        status.setAppliedAt(LocalDateTime.now());
        userOfferStatusRepository.save(status);

        UUID offerUuid = UUID.fromString(offerId);

        var offer = offerRepository.findById(offerUuid)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Offer not found for ID: " + offerId));

        applicationNoteHandler.saveApplicationNote(
                userId,
                offerId,
                offer.getOfferUrl(),
                offer.getCompany()
        );

        return status;
    }
}
