package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDTO;
import com.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class OfferRetrievalHandler {

    private final OfferRepository offerRepository;

    public OfferRetrievalHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    OfferDTO getOffer(Long id) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new RuntimeException("Offer not found"));
        return OfferMapper.mapToDto(offer);
    }

    List<OfferDTO> getAllOffers() {
        return offerRepository.findAll().stream()
                .map(OfferMapper::mapToDto)
                .collect(Collectors.toList());
    }
}