package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.statistics.CityDistributionProjection;
import com.example.offerbrowserprototype.domain.statistics.LevelDistributionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    Optional<Offer> findByOfferUrl(String offerUrl);

    boolean existsByOfferUrl(String offerUrl);

    List<Offer> findByDuplicateFalse();

    List<Offer> findAllByOrderByFetchedAtDesc();

    @Query("""
        SELECT COUNT(o)
        FROM Offer o
        WHERE o.duplicate = false
    """)
    long countNonDuplicateOffers();

    @Query("""
        SELECT o.level AS id, COUNT(o) AS count
        FROM Offer o
        WHERE o.duplicate = false
        GROUP BY o.level
    """)
    List<LevelDistributionProjection> getLevelDistributionSimple();

    @Query("""
        SELECT o.city AS id, COUNT(o) AS count
        FROM Offer o
        WHERE o.duplicate = false
        GROUP BY o.city
    """)
    List<CityDistributionProjection> getCityDistributionSimple();

    // alias pod Twoją metodę w handlerze
    default long countByIsDuplicateFalse() {
        return countByDuplicateFalse();
    }



    List<Offer> findByIdNotInAndDuplicateFalse(List<Long> ids);

    long countByDuplicateFalse();
}

