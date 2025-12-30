package com.example.offerbrowserprototype.domain.usseroffer;

import org.springframework.stereotype.Component;

@Component
public class UserAppliedOffersCountHandler {

    private final UserOfferStatusRepository userOfferStatusRepository;

    public UserAppliedOffersCountHandler(UserOfferStatusRepository userOfferStatusRepository) {
        this.userOfferStatusRepository = userOfferStatusRepository;
    }

    public long countAppliedOffersForUser(String userId) {
        return userOfferStatusRepository.findByUserIdAndAppliedTrue(userId).size();
    }
}
