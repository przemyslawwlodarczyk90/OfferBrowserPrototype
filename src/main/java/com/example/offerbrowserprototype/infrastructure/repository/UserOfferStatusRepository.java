package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.usseroffer.UserOfferStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOfferStatusRepository extends MongoRepository<UserOfferStatus, String> {

    Optional<UserOfferStatus> findByUserIdAndOfferId(String userId, String offerId);

    List<UserOfferStatus> findByUserIdAndAppliedTrue(String userId);

    List<UserOfferStatus> findByUserIdAndAppliedFalse(String userId);



}
