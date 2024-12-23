package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.offer.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends MongoRepository<Offer, String> {

    List<Offer> findAllByOrderByFetchedAtDesc();

    List<Offer> findByAppliedFalseOrderByFetchedAtDesc();

    List<Offer> findByAppliedTrueOrderByFetchedAtDesc();

    Optional<Offer> findByOfferUrl(String offerUrl);
}
