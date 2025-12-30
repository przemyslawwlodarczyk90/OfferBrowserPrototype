package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserOfferQueryHandler {

    private final OfferRepository offerRepository;
    private final UserOfferStatusRepository userOfferStatusRepository;

    public UserOfferQueryHandler(OfferRepository offerRepository,
                                 UserOfferStatusRepository userOfferStatusRepository) {
        this.offerRepository = offerRepository;
        this.userOfferStatusRepository = userOfferStatusRepository;
    }

    public List<Offer> getNotAppliedOffersForUser(String userId) {
        // Pobierz wszystkie zaaplikowane oferty dla użytkownika
        List<String> appliedOfferIds = userOfferStatusRepository.findByUserIdAndAppliedTrue(userId)
                .stream()
                .map(status -> status.getOfferId())
                .collect(Collectors.toList());

        // Pobierz oferty, które nie są zaaplikowane i nie są oznaczone jako duplikaty
        return offerRepository.findAll().stream()
                .filter(offer -> !appliedOfferIds.contains(offer.getId())) // Filtruj oferty, które nie są zaaplikowane
                .filter(offer -> !offer.isDuplicate()) // Filtruj oferty, które nie są oznaczone jako duplikaty
                .collect(Collectors.toList());
    }
}
