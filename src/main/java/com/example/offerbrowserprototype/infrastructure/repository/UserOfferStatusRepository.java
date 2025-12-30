package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.usseroffer.UserOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserOfferStatusRepository extends JpaRepository<UserOfferStatus, UUID> {

    Optional<UserOfferStatus> findByUserIdAndOfferId(String userId, String offerId);

    List<UserOfferStatus> findByUserIdAndAppliedTrue(String userId);

    List<UserOfferStatus> findByUserIdAndAppliedFalse(String userId);
}
