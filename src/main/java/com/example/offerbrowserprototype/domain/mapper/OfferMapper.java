package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.Offer;
import org.springframework.stereotype.Component;

@Component
public class OfferMapper {

    public OfferDTO toDTO(Offer offer) {
        OfferDTO dto = new OfferDTO();
        dto.setId(offer.getId());
        dto.setTitle(offer.getTitle());
        dto.setDescription(offer.getDescription());
        dto.setLocation(offer.getLocation());
        dto.setSalaryRange(offer.getSalaryRange());
        dto.setLevel(offer.getLevel());
        dto.setOfferUrl(offer.getOfferUrl());
        dto.setFetchedAt(offer.getFetchedAt());
        dto.setCompany(offer.getCompany());
        return dto;
    }

    public Offer toEntity(OfferDTO dto) {
        Offer offer = new Offer();
        offer.setTitle(dto.getTitle());
        offer.setDescription(dto.getDescription());
        offer.setLocation(dto.getLocation());
        offer.setSalaryRange(dto.getSalaryRange());
        offer.setLevel(dto.getLevel());
        offer.setOfferUrl(dto.getOfferUrl());
        offer.setFetchedAt(dto.getFetchedAt());
        offer.setCompany(dto.getCompany());
        return offer;
    }
}
