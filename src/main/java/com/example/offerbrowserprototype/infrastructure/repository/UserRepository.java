package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
