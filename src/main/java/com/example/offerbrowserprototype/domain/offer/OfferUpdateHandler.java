package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

@Component
class OfferUpdateHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public OfferUpdateHandler(OfferRepository offerRepository, OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    OfferDTO updateOffer(String id, OfferDTO offerDto) {
        Offer existingOffer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        // Aktualizuj pola oferty
        existingOffer.setTitle(offerDto.getTitle());
        existingOffer.setDescription(offerDto.getDescription());
        existingOffer.setLocation(offerDto.getLocation());
        existingOffer.setSalaryRange(offerDto.getSalaryRange());
        existingOffer.setTechnologies(offerDto.getTechnologies());

        // Zapisz zaktualizowaną ofertę
        Offer updatedOffer = offerRepository.save(existingOffer);
        return offerMapper.toDTO(updatedOffer);
    }
}
