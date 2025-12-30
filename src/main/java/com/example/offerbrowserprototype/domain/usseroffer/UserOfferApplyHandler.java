package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNoteHandler;
import com.example.offerbrowserprototype.domain.exception.OfferAlreadyAppliedException;
import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserOfferApplyHandler {

    private final UserOfferStatusRepository userOfferStatusRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ApplicationNoteHandler applicationNoteHandler;

    public UserOfferApplyHandler(
            UserOfferStatusRepository userOfferStatusRepository,
            OfferRepository offerRepository,
            UserRepository userRepository,
            ApplicationNoteHandler applicationNoteHandler
    ) {
        this.userOfferStatusRepository = userOfferStatusRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.applicationNoteHandler = applicationNoteHandler;
    }

    @Transactional
    public UserOfferStatus applyToOffer(Long userId, Long offerId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found: " + userId));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Offer not found: " + offerId));

        UserOfferStatus status = userOfferStatusRepository
                .findByUserAndOffer(user, offer)
                .orElseGet(() -> new UserOfferStatus(user, offer, false));

        if (status.isApplied()) {
            throw new OfferAlreadyAppliedException(
                    "Offer already applied for userId=" + userId
            );
        }

        status.setApplied(true);
        status.setAppliedAt(LocalDateTime.now());
        userOfferStatusRepository.save(status);

        applicationNoteHandler.saveApplicationNote(
                userId,
                offerId,
                offer.getOfferUrl(),
                offer.getCompany()
        );

        return status;
    }
}
