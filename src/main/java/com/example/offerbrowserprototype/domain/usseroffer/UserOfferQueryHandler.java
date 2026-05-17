package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        List<Long> excluded = new ArrayList<>();

        userOfferStatusRepository.findByUser_IdAndAppliedTrue(userId)
                .forEach(s -> excluded.add(s.getOffer().getId()));
        userOfferStatusRepository.findByUser_IdAndUselessTrue(userId)
                .forEach(s -> excluded.add(s.getOffer().getId()));

        if (excluded.isEmpty()) {
            return offerRepository.findByDuplicateFalse();
        }
        return offerRepository.findByIdNotInAndDuplicateFalse(excluded);
    }
}
