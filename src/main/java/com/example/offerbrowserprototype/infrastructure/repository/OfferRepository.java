package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.offer.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {

    List<Offer> findAllByOrderByFetchedAtDesc();

    Optional<Offer> findByOfferUrl(String offerUrl);

    long countByIsDuplicateFalse();

    interface LevelDistributionView {
        String getLevel();
        long getCount();
    }

    interface CityDistributionView {
        String getLocation();
        long getCount();
    }

    @Query("""
        select coalesce(o.level, 'unknown') as level, count(o) as count
        from Offer o
        where o.isDuplicate = false
        group by coalesce(o.level, 'unknown')
    """)
    List<LevelDistributionView> getLevelDistributionSimple();

    @Query("""
        select coalesce(o.location, 'unknown') as location, count(o) as count
        from Offer o
        where o.isDuplicate = false
        group by coalesce(o.location, 'unknown')
    """)
    List<CityDistributionView> getCityDistributionSimple();

    List<Offer> findByIdNotIn(List<UUID> ids);
}
