package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserOfferQueryHandler {

    private final UserOfferStatusRepository userOfferStatusRepository;
    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public UserOfferQueryHandler(UserOfferStatusRepository userOfferStatusRepository,
                                 OfferRepository offerRepository,
                                 OfferMapper offerMapper) {
        this.userOfferStatusRepository = userOfferStatusRepository;
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    public List<Offer> getNotAppliedOffersForUser(String userId) {
        // Pobierz ID zaaplikowanych ofert
        List<String> appliedOfferIds = userOfferStatusRepository
                .findByUserIdAndAppliedTrue(userId)
                .stream()
                .map(UserOfferStatus::getOfferId)
                .toList();

        // Pobierz oferty, które nie są zaaplikowane
        return offerRepository.findByIdNotIn(appliedOfferIds);
    }
}
