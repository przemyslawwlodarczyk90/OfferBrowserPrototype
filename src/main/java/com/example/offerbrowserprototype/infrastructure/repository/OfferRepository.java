package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.offer.Offer;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OfferRepository extends MongoRepository<Offer, String> {


    List<Offer> findAllByOrderByFetchedAtDesc();

    List<Offer> findByAppliedFalseOrderByFetchedAtDesc();

    List<Offer> findByAppliedTrueOrderByFetchedAtDesc();

    Optional<Offer> findByOfferUrl(String offerUrl);

    long countByIsDuplicateFalse();

    @Query("{ 'isDuplicate': false }")
    @Aggregation(pipeline = {
            "{ $group: { _id: '$level', count: { $sum: 1 } } }"
    })
    Map<String, Long> countByLevelAndIsDuplicateFalse();

    @Query("{ 'isDuplicate': false }")
    @Aggregation(pipeline = {
            "{ $group: { _id: '$location', count: { $sum: 1 } } }"
    })
    Map<String, Long> countByCityAndIsDuplicateFalse();
}
