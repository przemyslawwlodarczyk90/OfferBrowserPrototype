package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.stereotype.Component;

@Component
class OfferUpdateHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public OfferUpdateHandler(OfferRepository offerRepository, OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    OfferDTO updateOffer(Long id, OfferDTO offerDto) {
        Offer existingOffer = offerRepository.findById(id.toString())
                .orElseThrow(() -> new RuntimeException("Offer not found")); // Zgłoś wyjątek, jeśli oferta nie istnieje

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
