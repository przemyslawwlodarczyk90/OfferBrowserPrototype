package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.domain.requirement.NiceToHaveCreationHandler;
import com.example.offerbrowserprototype.domain.requirement.RequirementCreationHandler;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class OfferAdditionHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final Clock clock;
    private final RequirementCreationHandler requirementCreationHandler;
    private final NiceToHaveCreationHandler niceToHaveCreationHandler;

    public OfferAdditionHandler(
            OfferRepository offerRepository,
            OfferMapper offerMapper,
            Clock clock,
            RequirementCreationHandler requirementCreationHandler,
            NiceToHaveCreationHandler niceToHaveCreationHandler
    ) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.clock = clock;
        this.requirementCreationHandler = requirementCreationHandler;
        this.niceToHaveCreationHandler = niceToHaveCreationHandler;
    }

    public OfferDTO addOffer(OfferDTO dto) {
        Offer offer = offerMapper.toEntity(dto);
        offer.setFetchedAt(LocalDateTime.now(clock));
        Offer saved = offerRepository.save(offer);
        requirementCreationHandler.createFromOffer(saved);
        niceToHaveCreationHandler.createFromOffer(saved);
        return offerMapper.toDTO(saved);
    }
}
