package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDto;
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

    public OfferDto addOffer(OfferDto offerDto) {
        return additionHandler.addOffer(offerDto);
    }

    public OfferDto updateOffer(Long id, OfferDto offerDto) {
        return updateHandler.updateOffer(id, offerDto);
    }

    public void deleteOffer(Long id) {
        deletionHandler.deleteOffer(id);
    }

    public OfferDto getOffer(Long id) {
        return retrievalHandler.getOffer(id);
    }

    public List<OfferDto> getAllOffers() {
        return retrievalHandler.getAllOffers();
    }
}