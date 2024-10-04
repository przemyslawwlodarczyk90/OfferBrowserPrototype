package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class OfferAppliedHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public OfferAppliedHandler(OfferRepository offerRepository, OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    public List<OfferDTO> getAppliedOffers() {
        return offerRepository.findByAppliedTrueOrderByFetchedAtDesc()
                .stream()
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());
    }
}
