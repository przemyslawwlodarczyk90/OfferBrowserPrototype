package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheFacade;
import com.example.offerbrowserprototype.infrastructure.service.ExternalJobOfferService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OfferFacade {

    private final OfferAdditionHandler additionHandler;
    private final OfferUpdateHandler updateHandler;
    private final OfferDeletionHandler deletionHandler;
    private final OfferRetrievalHandler retrievalHandler;
    private final OfferNotAppliedHandler notAppliedHandler; // Nowy komponent
    private final OfferAppliedHandler appliedHandler; // Nowy komponent
    private final OfferApplicationHandler applicationHandler; // Nowy komponent
    private final OfferDetailsHandler detailsHandler;
    private final OfferCacheFacade offerCacheFacade;
    private final ExternalJobOfferService externalJobOfferService;

    public OfferFacade(OfferAdditionHandler additionHandler,
                       OfferUpdateHandler updateHandler,
                       OfferDeletionHandler deletionHandler,
                       OfferRetrievalHandler retrievalHandler,
                       OfferNotAppliedHandler notAppliedHandler, // Dodany komponent do konstruktora
                       OfferAppliedHandler appliedHandler, // Dodany komponent do konstruktora
                       OfferApplicationHandler applicationHandler, // Dodany komponent do konstruktora
                       OfferDetailsHandler detailsHandler,
                       OfferCacheFacade offerCacheFacade,
                       ExternalJobOfferService externalJobOfferService) {
        this.additionHandler = additionHandler;
        this.updateHandler = updateHandler;
        this.deletionHandler = deletionHandler;
        this.retrievalHandler = retrievalHandler;
        this.notAppliedHandler = notAppliedHandler;
        this.appliedHandler = appliedHandler;
        this.applicationHandler = applicationHandler;
        this.detailsHandler = detailsHandler;
        this.offerCacheFacade = offerCacheFacade;
        this.externalJobOfferService = externalJobOfferService;
    }

    public OfferDTO addOffer(OfferDTO offerDto) {
        return additionHandler.addOffer(offerDto);
    }

    public OfferDTO updateOffer(String id, OfferDTO offerDto) {
        return updateHandler.updateOffer(id, offerDto);
    }

    public OfferDTO getOffer(String id) {
        return detailsHandler.getOfferById(id);
    }

    public void deleteOffer(String id) {
        deletionHandler.deleteOffer(id);
    }

    public List<OfferDTO> getNotAppliedOffers() {
        return notAppliedHandler.getNotAppliedOffers();
    }

    public List<OfferDTO> getAppliedOffers() {
        return appliedHandler.getAppliedOffers();
    }

    public void applyToOffer(String offerId) {
        applicationHandler.applyToOffer(offerId);
    }

    // Główna metoda do pobierania wszystkich ofert (łączy oferty lokalne i zewnętrzne)
    public List<OfferDTO> getAllOffers() {
        List<OfferDTO> cachedOffers = offerCacheFacade.getCachedOffers();
        if (cachedOffers != null && !cachedOffers.isEmpty()) {
            return cachedOffers;
        }

        List<OfferDTO> localOffers = retrievalHandler.getAllOffers();
        List<OfferDTO> externalOffers = externalJobOfferService.fetchExternalOffers();

        List<OfferDTO> combinedOffers = new ArrayList<>(localOffers);
        combinedOffers.addAll(externalOffers);

        offerCacheFacade.cacheOffers(combinedOffers);

        return combinedOffers;
    }
}
