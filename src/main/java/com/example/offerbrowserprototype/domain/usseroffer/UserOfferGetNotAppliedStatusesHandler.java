package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserOfferGetNotAppliedStatusesHandler {

    private final UserOfferStatusRepository repository;

    public UserOfferGetNotAppliedStatusesHandler(UserOfferStatusRepository repository) {
        this.repository = repository;
    }

    public List<UserOfferStatus> getNotAppliedStatuses(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        return repository.findByUserIdAndAppliedFalse(userId.trim());
    }
}
