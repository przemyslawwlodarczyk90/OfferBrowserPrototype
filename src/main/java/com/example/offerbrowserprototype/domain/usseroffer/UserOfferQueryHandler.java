package com.example.offerbrowserprototype.domain.usseroffer;


import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserOfferQueryHandler {

    private final UserOfferStatusRepository userOfferStatusRepository;
    private final OfferRepository offerRepository;

    public UserOfferQueryHandler(UserOfferStatusRepository userOfferStatusRepository, OfferRepository offerRepository) {
        this.userOfferStatusRepository = userOfferStatusRepository;
        this.offerRepository = offerRepository;
    }

    public List<Offer> getNotAppliedOffersForUser(String userId) {
        List<UserOfferStatus> statuses = userOfferStatusRepository.findByUserIdAndAppliedFalse(userId);
        List<String> notAppliedOfferIds = statuses.stream()
                .map(UserOfferStatus::getOfferId)
                .toList();

        return offerRepository.findAllById(notAppliedOfferIds);
    }

    public List<Offer> getAppliedOffersForUser(String userId) {
        List<UserOfferStatus> statuses = userOfferStatusRepository.findByUserIdAndAppliedTrue(userId);
        List<String> appliedOfferIds = statuses.stream()
                .map(UserOfferStatus::getOfferId)
                .toList();

        return offerRepository.findAllById(appliedOfferIds);
    }
}
