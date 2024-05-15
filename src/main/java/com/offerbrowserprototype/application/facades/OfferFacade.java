package com.offerbrowserprototype.application.facades;

import com.offerbrowserprototype.application.services.OfferService;
import com.offerbrowserprototype.domain.offer.OfferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfferFacade {
    private final OfferService service;


    public OfferFacade(OfferService service) {
        this.service = service;
    }

    public OfferDto addOffer(OfferDto offerDto) {
        return service.saveOffer(offerDto);
    }

    public OfferDto updateOffer(Long id, OfferDto offerDto) {
        return service.updateOffer(id, offerDto);
    }

    public void deleteOffer(Long id) {
        service.deleteOffer(id);
    }

    public OfferDto getOffer(Long id) {
        return service.getOfferById(id);
    }

    public List<OfferDto> getAllOffers() {
        return service.getAllOffers();
    }
}