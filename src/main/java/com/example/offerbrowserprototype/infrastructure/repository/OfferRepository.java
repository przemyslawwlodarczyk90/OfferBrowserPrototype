package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.statistics.CityDistribution;
import com.example.offerbrowserprototype.domain.statistics.LevelDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OfferRepository extends MongoRepository<Offer, String> {

    Logger logger = LoggerFactory.getLogger(OfferRepository.class);


    List<Offer> findAllByOrderByFetchedAtDesc();



    Optional<Offer> findByOfferUrl(String offerUrl);



    long countByIsDuplicateFalse();

    @Aggregation(pipeline = {
            "{ $match: { isDuplicate: false } }",
            "{ $group: { _id: { $ifNull: ['$level', 'unknown'] }, count: { $sum: 1 } } }"
    })
    List<LevelDistribution> getLevelDistributionSimple();


    @Aggregation(pipeline = {
            "{ $match: { isDuplicate: false } }",
            "{ $group: { _id: { $ifNull: ['$location', 'unknown'] }, count: { $sum: 1 } } }"
    })
    List<CityDistribution> getCityDistributionSimple();

    @Query("{ '_id' : { '$nin' : ?0 } }")
    List<Offer> findByIdNotIn(List<String> ids);

}

