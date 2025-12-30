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

    public List<UserOfferStatus> getNotAppliedStatuses(Long userId) {
        return repository.findByUser_IdAndAppliedFalse(userId);
    }
}
