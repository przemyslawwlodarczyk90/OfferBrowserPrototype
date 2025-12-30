package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class OfferUpdateHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final Clock clock;

    public OfferUpdateHandler(OfferRepository offerRepository, OfferMapper offerMapper, Clock clock) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.clock = clock;
    }

    public OfferDTO updateOffer(String id, OfferDTO offerDto) {
        Offer existingOffer = offerRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        existingOffer.setTitle(offerDto.getTitle());
        existingOffer.setDescription(offerDto.getDescription());
        existingOffer.setLocation(offerDto.getLocation());
        existingOffer.setSalaryRange(offerDto.getSalaryRange());
        existingOffer.setLevel(offerDto.getLevel());
        existingOffer.setFetchedAt(LocalDateTime.now(clock));

        return offerMapper.toDTO(offerRepository.save(existingOffer));
    }
}
