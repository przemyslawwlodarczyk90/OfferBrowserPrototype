package com.offerbrowserprototype.infrastructure.repository;

import com.offerbrowserprototype.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}