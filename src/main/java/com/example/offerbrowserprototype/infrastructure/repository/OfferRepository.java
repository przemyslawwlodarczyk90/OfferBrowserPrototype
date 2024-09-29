package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.offer.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends MongoRepository<Offer, String> {

    List<Offer> findAllByOrderByFetchedAtDesc(); // Wszystkie oferty posortowane chronologicznie

    List<Offer> findByAppliedFalseOrderByFetchedAtDesc(); // Oferty, na które nie aplikowano, posortowane chronologicznie

    List<Offer> findByAppliedTrueOrderByFetchedAtDesc(); // Oferty, na które aplikowano, posortowane chronologicznie
}
