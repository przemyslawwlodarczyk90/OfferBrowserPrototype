package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfferFacade {

    private final OfferAdditionHandler additionHandler;
    private final OfferUpdateHandler updateHandler;
    private final OfferDeletionHandler deletionHandler;
    private final OfferRetrievalHandler retrievalHandler;

    public OfferFacade(OfferAdditionHandler additionHandler,
                       OfferUpdateHandler updateHandler,
                       OfferDeletionHandler deletionHandler,
                       OfferRetrievalHandler retrievalHandler) {
        this.additionHandler = additionHandler;
        this.updateHandler = updateHandler;
        this.deletionHandler = deletionHandler;
        this.retrievalHandler = retrievalHandler;
    }

    public OfferDTO addOffer(OfferDTO offerDto) {
        return additionHandler.addOffer(offerDto);
    }

    public OfferDTO updateOffer(Long id, OfferDTO offerDto) {
        return updateHandler.updateOffer(id, offerDto);
    }

    public void deleteOffer(Long id) {
        deletionHandler.deleteOffer(id);
    }

    public OfferDTO getOffer(Long id) {
        return retrievalHandler.getOffer(id);
    }

    public List<OfferDTO> getAllOffers() {
        return retrievalHandler.getAllOffers();
    }
}