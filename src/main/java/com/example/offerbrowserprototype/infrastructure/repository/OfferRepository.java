package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.offer.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfferRepository extends MongoRepository<Offer, String> {

}
