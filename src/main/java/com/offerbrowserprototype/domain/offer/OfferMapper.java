package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDTO;

public class OfferMapper {

    public static Offer mapToEntity(OfferDTO offerDto) {
        Offer offer = new Offer();
        offer.setTitle(offerDto.getTitle());
        offer.setDescription(offerDto.getDescription());
        offer.setLocation(offerDto.getLocation());
        offer.setSalaryRange(offerDto.getSalaryRange());
        offer.setTechnologies(offerDto.getTechnologies());
        return offer;
    }

    public static OfferDTO mapToDto(Offer offer) {
        return new OfferDTO(offer.getId(), offer.getTitle(), offer.getDescription(),
                offer.getLocation(), offer.getSalaryRange(), offer.getTechnologies());
    }
}