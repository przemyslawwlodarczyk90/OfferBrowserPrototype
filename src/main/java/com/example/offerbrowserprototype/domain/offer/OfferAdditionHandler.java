package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
class OfferAdditionHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final Clock clock;

    public OfferAdditionHandler(OfferRepository offerRepository, OfferMapper offerMapper, Clock clock) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.clock = clock;
    }

    OfferDTO addOffer(OfferDTO offerDto) {
        Offer offer = offerMapper.toEntity(offerDto);
        // Użycie clock do ustawienia daty
        offer.setFetchedAt(LocalDateTime.now(clock));
        Offer savedOffer = offerRepository.save(offer);
        return offerMapper.toDTO(savedOffer);
    }}