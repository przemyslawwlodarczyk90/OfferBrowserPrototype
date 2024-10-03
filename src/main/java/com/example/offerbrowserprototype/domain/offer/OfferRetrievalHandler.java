package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheFacade;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.service.ExternalJobOfferService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class OfferRetrievalHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public OfferRetrievalHandler(OfferRepository offerRepository,
                                 OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    public OfferDTO getOffer(String id) {
        return offerRepository.findById(id)
                .map(offerMapper::toDTO)
                .orElse(null);
    }

    public List<OfferDTO> getAllOffers() {
        // Pobieranie wszystkich ofert z bazy danych
        return offerRepository.findAllByOrderByFetchedAtDesc()
                .stream()
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());
    }
}

