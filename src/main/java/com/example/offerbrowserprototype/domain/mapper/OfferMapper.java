package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.Offer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        dto.setRequirements(copyList(offer.getRequirements()));
        dto.setNiceToHave(copyList(offer.getNiceToHave()));
        dto.setSource(offer.getSource());
        return dto;
    }

    private List<String> copyList(List<String> source) {
        return source != null ? new ArrayList<>(source) : null;
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
                .requirements(dto.getRequirements())
                .niceToHave(dto.getNiceToHave())
                .source(dto.getSource())
                .build();
    }
}
