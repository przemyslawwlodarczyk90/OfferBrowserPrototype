package com.offerbrowserprototype.domain.offer;



import com.offerbrowserprototype.domain.offer.dto.OfferDTO;
import com.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

@Component
class OfferUpdateHandler {


    private final OfferRepository offerRepository;

    public OfferUpdateHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    OfferDTO updateOffer(Long id, OfferDTO offerDto) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new RuntimeException("Offer not found"));
        offer.setTitle(offerDto.getTitle());
        offer.setDescription(offerDto.getDescription());
        offer.setLocation(offerDto.getLocation());
        offer.setSalaryRange(offerDto.getSalaryRange());
        offer.setTechnologies(offerDto.getTechnologies());

        Offer updatedOffer = offerRepository.save(offer);
        return OfferMapper.mapToDto(updatedOffer);
    }
}