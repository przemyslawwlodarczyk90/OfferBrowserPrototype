package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserAppliedOffersHandler {

    private final OfferRepository offerRepository;
    private final UserOfferStatusRepository userOfferStatusRepository;

    public UserAppliedOffersHandler(OfferRepository offerRepository,
                                    UserOfferStatusRepository userOfferStatusRepository) {
        this.offerRepository = offerRepository;
        this.userOfferStatusRepository = userOfferStatusRepository;
    }

    public List<Offer> getAppliedOffersForUser(String userId) {
        // Pobierz identyfikatory zaaplikowanych ofert
        List<String> appliedOfferIds = userOfferStatusRepository.findByUserIdAndAppliedTrue(userId)
                .stream()
                .map(UserOfferStatus::getOfferId)
                .toList();

        // Pobierz oferty z repozytorium na podstawie identyfikatorów
        return offerRepository.findAllById(appliedOfferIds);
    }
}
