package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserOfferQueryHandler {

    private final OfferRepository offerRepository;
    private final UserOfferStatusRepository userOfferStatusRepository;

    public UserOfferQueryHandler(
            OfferRepository offerRepository,
            UserOfferStatusRepository userOfferStatusRepository
    ) {
        this.offerRepository = offerRepository;
        this.userOfferStatusRepository = userOfferStatusRepository;
    }

    public List<Offer> getNotAppliedOffersForUser(Long userId) {

        List<Long> appliedOfferIds = userOfferStatusRepository
                .findByUser_IdAndAppliedTrue(userId)
                .stream()
                .map(status -> status.getOffer().getId())
                .toList();

        if (appliedOfferIds.isEmpty()) {
            return offerRepository.findByDuplicateFalse();
        }

        return offerRepository.findByIdNotInAndDuplicateFalse(appliedOfferIds);
    }
}
