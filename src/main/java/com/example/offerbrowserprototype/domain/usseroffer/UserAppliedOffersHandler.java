package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserAppliedOffersHandler {

    private final OfferRepository offerRepository;
    private final UserOfferStatusRepository userOfferStatusRepository;

    public UserAppliedOffersHandler(
            OfferRepository offerRepository,
            UserOfferStatusRepository userOfferStatusRepository
    ) {
        this.offerRepository = offerRepository;
        this.userOfferStatusRepository = userOfferStatusRepository;
    }

    public List<Offer> getAppliedOffersForUser(Long userId) {
        return getAppliedOffersForUser(userId, null);
    }

    /** @param source portal to filter by, or {@code null} for all portals */
    public List<Offer> getAppliedOffersForUser(Long userId, OfferSource source) {

        List<Long> appliedOfferIds = userOfferStatusRepository
                .findByUser_IdAndAppliedTrue(userId)
                .stream()
                .map(status -> status.getOffer().getId())
                .toList();

        if (source != null) {
            return appliedOfferIds.isEmpty()
                    ? List.of()
                    : offerRepository.findByIdInAndSource(appliedOfferIds, source.value());
        }
        return offerRepository.findAllById(appliedOfferIds);
    }
}
