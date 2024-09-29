package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public OfferService(OfferRepository offerRepository, OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    // Pobieranie wszystkich ofert posortowanych chronologicznie
    public List<OfferDTO> getAllOffers() {
        return offerRepository.findAllByOrderByFetchedAtDesc()
                .stream()
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Pobieranie ofert, na które nie aplikowano, posortowanych chronologicznie
    public List<OfferDTO> getNotAppliedOffers() {
        return offerRepository.findByAppliedFalseOrderByFetchedAtDesc()
                .stream()
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Pobieranie ofert, na które aplikowano, posortowanych chronologicznie
    public List<OfferDTO> getAppliedOffers() {
        return offerRepository.findByAppliedTrueOrderByFetchedAtDesc()
                .stream()
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Oznaczanie oferty jako aplikowanej
    public void applyToOffer(String offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));

        offer.setApplied(true);
        offerRepository.save(offer);
    }

    // Pobieranie szczegółów oferty na podstawie ID
    public OfferDTO getOfferById(String offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));
        return offerMapper.toDTO(offer);
    }
}
