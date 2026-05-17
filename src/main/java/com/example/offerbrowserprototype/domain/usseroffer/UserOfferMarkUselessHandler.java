package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserOfferMarkUselessHandler {

    private final UserOfferStatusRepository userOfferStatusRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    public UserOfferMarkUselessHandler(
            UserOfferStatusRepository userOfferStatusRepository,
            OfferRepository offerRepository,
            UserRepository userRepository
    ) {
        this.userOfferStatusRepository = userOfferStatusRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void markAsUseless(Long userId, Long offerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found: " + offerId));

        UserOfferStatus status = userOfferStatusRepository
                .findByUserAndOffer(user, offer)
                .orElseGet(() -> new UserOfferStatus(user, offer, false));

        status.setUseless(true);
        status.setUselessAt(LocalDateTime.now());
        userOfferStatusRepository.save(status);
    }
}
