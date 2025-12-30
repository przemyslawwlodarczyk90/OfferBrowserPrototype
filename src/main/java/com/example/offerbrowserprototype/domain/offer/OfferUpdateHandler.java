package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class OfferUpdateHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final Clock clock;

    public OfferUpdateHandler(
            OfferRepository offerRepository,
            OfferMapper offerMapper,
            Clock clock
    ) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.clock = clock;
    }

    public OfferDTO updateOffer(Long id, OfferDTO dto) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));

        offer.setTitle(dto.getTitle());
        offer.setDescription(dto.getDescription());
        offer.setLocation(dto.getLocation());
        offer.setSalaryRange(dto.getSalaryRange());
        offer.setLevel(dto.getLevel());
        offer.setFetchedAt(LocalDateTime.now(clock));

        return offerMapper.toDTO(offerRepository.save(offer));
    }
}
