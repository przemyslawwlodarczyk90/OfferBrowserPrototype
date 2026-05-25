package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.offer.FlagType;
import com.example.offerbrowserprototype.domain.offer.OfferFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferFlagRepository extends JpaRepository<OfferFlag, Long> {

    boolean existsByUser_IdAndOffer_IdAndType(Long userId, Long offerId, FlagType type);

    @Query("SELECT DISTINCT f.offer.id FROM OfferFlag f WHERE f.type = :type")
    List<Long> findFlaggedOfferIdsByType(@Param("type") FlagType type);

    @Query("SELECT f.offer.id, f.type, COUNT(f) FROM OfferFlag f GROUP BY f.offer.id, f.type")
    List<Object[]> getFlagCountsByOfferAndType();

    @Query("SELECT DISTINCT f.offer.id FROM OfferFlag f WHERE f.user.id = :userId AND f.type = :type")
    List<Long> findOfferIdsByUserIdAndType(@Param("userId") Long userId, @Param("type") FlagType type);

    @Query("SELECT f FROM OfferFlag f JOIN FETCH f.user WHERE f.offer.id = :offerId ORDER BY f.flaggedAt DESC")
    List<OfferFlag> findByOffer_Id(@Param("offerId") Long offerId);

    long countByUser_IdAndType(Long userId, FlagType type);

    void deleteByOffer_Id(Long offerId);
}
