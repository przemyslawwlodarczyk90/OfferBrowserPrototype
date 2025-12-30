package com.example.offerbrowserprototype.domain.usseroffer;


import org.springframework.stereotype.Component;

@Component
public class UserOfferStatusCreationHandler {

    private final UserOfferStatusRepository userOfferStatusRepository;

    public UserOfferStatusCreationHandler(UserOfferStatusRepository userOfferStatusRepository) {
        this.userOfferStatusRepository = userOfferStatusRepository;
    }

    public UserOfferStatus createStatusIfNotExists(String userId, String offerId) {
        return userOfferStatusRepository.findByUserIdAndOfferId(userId, offerId)
                .orElseGet(() -> {
                    UserOfferStatus status = new UserOfferStatus(userId, offerId, false);
                    userOfferStatusRepository.save(status);
                    return status;
                });
    }
}
