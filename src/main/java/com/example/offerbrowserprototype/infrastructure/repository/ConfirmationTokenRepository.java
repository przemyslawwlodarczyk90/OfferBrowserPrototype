package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.user.ConfirmationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, String> {
    Optional<ConfirmationToken> findByToken(String token);
    void deleteByUserId(String userId);
}
