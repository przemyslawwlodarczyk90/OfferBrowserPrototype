package com.offerbrowserprototype.infrastructure.repository;

import com.offerbrowserprototype.domain.offer.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}