package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.Offer;
import org.springframework.stereotype.Component;

@Component
public class OfferMapper {

    public OfferDTO toDTO(Offer offer) {
        OfferDTO dto = new OfferDTO(
                offer.getId(),
                offer.getTitle(),
                offer.getDescription(),
                offer.getLocation(),
                offer.getOfferUrl(),
                offer.getSalaryRange(),
                offer.getCompany(),
                offer.getLevel(),
                false,
                offer.getFetchedAt()
        );
        dto.setCity(offer.getCity());
        return dto;
    }

    public Offer toEntity(OfferDTO dto) {
        return Offer.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .offerUrl(dto.getOfferUrl())
                .salaryRange(dto.getSalaryRange())
                .level(dto.getLevel())
                .company(dto.getCompany())
                .fetchedAt(dto.getFetchedAt())
                .build();
    }
}
