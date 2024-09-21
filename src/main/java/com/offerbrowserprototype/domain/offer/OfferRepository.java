package com.offerbrowserprototype.domain.offer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfferRepository extends MongoRepository<Offer, String> {
    // Możesz dodać tutaj dodatkowe metody wyszukiwania, jeśli potrzebujesz
}
