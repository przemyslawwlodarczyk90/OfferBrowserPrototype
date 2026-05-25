package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.repository.OfferFlagRepository;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OfferFlagHandler {

    private final OfferFlagRepository offerFlagRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    public OfferFlagHandler(
            OfferFlagRepository offerFlagRepository,
            OfferRepository offerRepository,
            UserRepository userRepository
    ) {
        this.offerFlagRepository = offerFlagRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void flag(Long userId, Long offerId, FlagType type) {
        if (offerFlagRepository.existsByUser_IdAndOffer_IdAndType(userId, offerId, type)) {
            return;
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found: " + offerId));

        offerFlagRepository.save(OfferFlag.builder()
                .user(user)
                .offer(offer)
                .type(type)
                .flaggedAt(LocalDateTime.now())
                .build());
    }

    @Transactional
    public void flagByUrl(Long userId, String offerUrl, FlagType type) {
        Offer offer = offerRepository.findByOfferUrl(offerUrl)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found for URL: " + offerUrl));
        flag(userId, offer.getId(), type);
    }
}
