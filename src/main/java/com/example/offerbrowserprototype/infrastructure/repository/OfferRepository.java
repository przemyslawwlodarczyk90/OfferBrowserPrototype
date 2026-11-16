package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.statistics.CityDistributionProjection;
import com.example.offerbrowserprototype.domain.statistics.LevelDistributionProjection;
import com.example.offerbrowserprototype.domain.statistics.SourceLevelCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    Optional<Offer> findByOfferUrl(String offerUrl);

    boolean existsByOfferUrl(String offerUrl);

    List<Offer> findAllByOrderByFetchedAtDesc();

    List<Offer> findByIdNotIn(List<Long> ids);

    @Query("SELECT COUNT(o) FROM Offer o WHERE o.id NOT IN " +
           "(SELECT f.offer.id FROM OfferFlag f WHERE f.type = 'DUPLICATE')")
    long countNonDuplicate();

    @Query("""
        SELECT o.level AS id, COUNT(o) AS count
        FROM Offer o
        WHERE o.id NOT IN (SELECT f.offer.id FROM OfferFlag f WHERE f.type = 'DUPLICATE')
        GROUP BY o.level
    """)
    List<LevelDistributionProjection> getLevelDistributionSimple();

    @Query("""
        SELECT o.city AS id, COUNT(o) AS count
        FROM Offer o
        WHERE o.id NOT IN (SELECT f.offer.id FROM OfferFlag f WHERE f.type = 'DUPLICATE')
        GROUP BY o.city
    """)
    List<CityDistributionProjection> getCityDistributionSimple();

    /** Offers per portal and level (duplicates excluded) — the "all portals" statistics. */
    @Query("""
        SELECT o.source AS source, o.level AS level, COUNT(o) AS count
        FROM Offer o
        WHERE o.id NOT IN (SELECT f.offer.id FROM OfferFlag f WHERE f.type = 'DUPLICATE')
        GROUP BY o.source, o.level
    """)
    List<SourceLevelCountProjection> getLevelDistributionPerSource();

    @Query("SELECT DISTINCT o.source FROM Offer o WHERE o.source IS NOT NULL ORDER BY o.source")
    List<String> findDistinctSources();

    // ── Per-portal variants ─────────────────────────────────────────────────

    List<Offer> findBySourceOrderByFetchedAtDesc(String source);

    List<Offer> findBySourceAndIdNotInOrderByFetchedAtDesc(String source, Collection<Long> ids);

    List<Offer> findByIdInAndSource(Collection<Long> ids, String source);

    @Query("SELECT COUNT(o) FROM Offer o WHERE o.source = :source AND o.id NOT IN " +
           "(SELECT f.offer.id FROM OfferFlag f WHERE f.type = 'DUPLICATE')")
    long countNonDuplicateBySource(@Param("source") String source);

    @Query("""
        SELECT o.level AS id, COUNT(o) AS count
        FROM Offer o
        WHERE o.source = :source
          AND o.id NOT IN (SELECT f.offer.id FROM OfferFlag f WHERE f.type = 'DUPLICATE')
        GROUP BY o.level
    """)
    List<LevelDistributionProjection> getLevelDistributionBySource(@Param("source") String source);

    @Query("""
        SELECT o.city AS id, COUNT(o) AS count
        FROM Offer o
        WHERE o.source = :source
          AND o.id NOT IN (SELECT f.offer.id FROM OfferFlag f WHERE f.type = 'DUPLICATE')
        GROUP BY o.city
    """)
    List<CityDistributionProjection> getCityDistributionBySource(@Param("source") String source);

    /** Only the URLs of a portal's offers — lets the scraper skip offers already in the database. */
    @Query("SELECT o.offerUrl FROM Offer o WHERE o.source = :source OR o.offerUrl LIKE :urlPrefix")
    List<String> findOfferUrlsBySource(@Param("source") String source, @Param("urlPrefix") String urlPrefix);

    /** One-off migration: offers imported before the source column existed. */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Offer o SET o.source = :source WHERE o.source IS NULL AND o.offerUrl LIKE :urlPattern")
    int backfillSource(@Param("source") String source, @Param("urlPattern") String urlPattern);
}
