package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OfferRetrievalHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public OfferRetrievalHandler(
            OfferRepository offerRepository,
            OfferMapper offerMapper
    ) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    @Transactional(readOnly = true)
    public OfferDTO getOffer(Long id) {
        return offerRepository.findById(id)
                .map(offerMapper::toDTO)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<OfferDTO> getAllOffers() {
        return offerRepository.findAllByOrderByFetchedAtDesc()
                .stream()
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());
    }
}
