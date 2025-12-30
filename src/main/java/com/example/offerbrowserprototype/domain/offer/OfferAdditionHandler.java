package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class OfferAdditionHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final Clock clock;

    public OfferAdditionHandler(
            OfferRepository offerRepository,
            OfferMapper offerMapper,
            Clock clock
    ) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.clock = clock;
    }

    public OfferDTO addOffer(OfferDTO dto) {
        Offer offer = offerMapper.toEntity(dto);
        offer.setFetchedAt(LocalDateTime.now(clock));
        return offerMapper.toDTO(offerRepository.save(offer));
    }
}
