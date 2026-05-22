package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserOfferStatusRepository extends JpaRepository<UserOfferStatus, Long> {



    List<UserOfferStatus> findByUser_IdAndAppliedFalse(Long userId);

    Optional<UserOfferStatus> findByUserAndOffer(User user, Offer offer);

    long countByUser_IdAndAppliedTrue(Long userId);

    long countByUser_IdAndAppliedFalse(Long userId);

    long countByUser_IdAndUselessTrue(Long userId);

    List<UserOfferStatus> findByUser_IdAndAppliedTrue(Long userId);

    List<UserOfferStatus> findByUser_IdAndUselessTrue(Long userId);

    List<UserOfferStatus> findByOffer_IdAndUselessTrue(Long offerId);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT s.offer.id FROM UserOfferStatus s WHERE s.useless = true")
    java.util.List<Long> findOfferIdsWithAnyUseless();

    void deleteByOffer_Id(Long offerId);

}
