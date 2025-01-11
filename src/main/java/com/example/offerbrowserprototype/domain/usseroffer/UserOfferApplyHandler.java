package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class UserOfferApplyHandler {

    private final UserOfferStatusRepository userOfferStatusRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserOfferApplyHandler.class);

    public UserOfferApplyHandler(UserOfferStatusRepository userOfferStatusRepository, UserRepository userRepository) {
        this.userOfferStatusRepository = userOfferStatusRepository;
        this.userRepository = userRepository;
    }

    public void applyToOffer(String userId, String offerId) {
        if (userId == null || userId.isEmpty()) {
            logger.error("Invalid userId: {}", userId);
            throw new IllegalArgumentException("Invalid userId");
        }

        logger.info("Attempting to apply to offer. userId: {}, offerId: {}", userId, offerId);

        User user = userRepository.findById(userId).orElseThrow(() -> {
            logger.error("User with ID {} not found", userId);
            return new IllegalArgumentException("User with ID " + userId + " not found");
        });

        logger.info("User found: {}", user);

        UserOfferStatus userOfferStatus = userOfferStatusRepository
                .findByUserIdAndOfferId(userId, offerId)
                .orElse(new UserOfferStatus(userId, offerId, false));

        userOfferStatus.setApplied(true);
        userOfferStatus.setAppliedAt(LocalDateTime.now());
        userOfferStatusRepository.save(userOfferStatus);

        logger.info("UserOfferStatus saved. userId: {}, offerId: {}, appliedAt: {}",
                userOfferStatus.getUserId(), userOfferStatus.getOfferId(), userOfferStatus.getAppliedAt());
    }
}