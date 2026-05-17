package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserOfferGetUselessHandler {

    private final UserOfferStatusRepository userOfferStatusRepository;

    public UserOfferGetUselessHandler(UserOfferStatusRepository userOfferStatusRepository) {
        this.userOfferStatusRepository = userOfferStatusRepository;
    }

    public List<Offer> getUselessOffersForUser(Long userId) {
        return userOfferStatusRepository.findByUser_IdAndUselessTrue(userId)
                .stream()
                .map(UserOfferStatus::getOffer)
                .toList();
    }
}
