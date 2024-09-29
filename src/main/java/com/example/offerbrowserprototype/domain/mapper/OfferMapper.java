package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.Offer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OfferMapper {

    // Metoda do konwersji z `Offer` na `OfferDTO`
    public OfferDTO toDTO(Offer offer) {
        return new OfferDTO(
                offer.getId(), // Użyj `String` zamiast `Long`
                offer.getTitle(),
                offer.getDescription(),
                offer.getLocation(),
                offer.getSalaryRange(),
                offer.getTechnologies()
        );
    }

    // Metoda do konwersji listy `Offer` na listę `OfferDTO`
    public List<OfferDTO> toDTOList(List<Offer> offers) {
        return offers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Metoda do konwersji z `OfferDTO` na `Offer`
    public Offer toEntity(OfferDTO offerDTO) {
        Offer offer = new Offer();
        offer.setId(offerDTO.getId()); // Użyj `String` bez konwersji
        offer.setTitle(offerDTO.getTitle());
        offer.setDescription(offerDTO.getDescription());
        offer.setLocation(offerDTO.getLocation());
        offer.setSalaryRange(offerDTO.getSalaryRange());
        offer.setTechnologies(offerDTO.getTechnologies());
        return offer;
    }

    // Metoda do konwersji listy `OfferDTO` na listę `Offer`
    public List<Offer> toEntityList(List<OfferDTO> offerDTOs) {
        return offerDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
