package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.offer.FlagType;
import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.infrastructure.repository.OfferFlagRepository;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserOfferQueryHandler {

    private final OfferRepository offerRepository;
    private final UserOfferStatusRepository userOfferStatusRepository;
    private final OfferFlagRepository offerFlagRepository;

    public UserOfferQueryHandler(
            OfferRepository offerRepository,
            UserOfferStatusRepository userOfferStatusRepository,
            OfferFlagRepository offerFlagRepository
    ) {
        this.offerRepository = offerRepository;
        this.userOfferStatusRepository = userOfferStatusRepository;
        this.offerFlagRepository = offerFlagRepository;
    }

    public List<Offer> getNotAppliedOffersForUser(Long userId) {
        List<Long> excluded = new ArrayList<>();

        userOfferStatusRepository.findByUser_IdAndAppliedTrue(userId)
                .forEach(s -> excluded.add(s.getOffer().getId()));

        excluded.addAll(offerFlagRepository.findOfferIdsByUserIdAndType(userId, FlagType.USELESS));
        excluded.addAll(offerFlagRepository.findFlaggedOfferIdsByType(FlagType.DUPLICATE));

        if (excluded.isEmpty()) {
            return offerRepository.findAllByOrderByFetchedAtDesc();
        }
        return offerRepository.findByIdNotIn(excluded);
    }
}
