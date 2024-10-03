package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
class OfferUpdateHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final Clock clock;

    public OfferUpdateHandler(OfferRepository offerRepository, OfferMapper offerMapper, Clock clock) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.clock = clock;
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
        // Aktualizacja danych
        existingOffer.setFetchedAt(LocalDateTime.now(clock));

        // Zapisz zaktualizowaną ofertę
        Offer updatedOffer = offerRepository.save(existingOffer);
        return offerMapper.toDTO(updatedOffer);
    }
}
