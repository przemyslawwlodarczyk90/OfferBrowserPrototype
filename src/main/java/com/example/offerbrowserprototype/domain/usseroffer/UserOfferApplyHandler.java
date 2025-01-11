package com.example.offerbrowserprototype.domain.usseroffer;


import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserOfferApplyHandler {

    private final UserOfferStatusRepository userOfferStatusRepository;

    public UserOfferApplyHandler(UserOfferStatusRepository userOfferStatusRepository) {
        this.userOfferStatusRepository = userOfferStatusRepository;
    }

    public void applyToOffer(String userId, String offerId) {
        UserOfferStatus userOfferStatus = userOfferStatusRepository
                .findByUserIdAndOfferId(userId, offerId)
                .orElse(new UserOfferStatus(userId, offerId, false));

        if (!userOfferStatus.isApplied()) {
            userOfferStatus.setApplied(true);
            userOfferStatus.setAppliedAt(LocalDateTime.now());
            userOfferStatusRepository.save(userOfferStatus);
        }
    }
}

