package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheFacade;
import com.example.offerbrowserprototype.infrastructure.service.ExternalJobOfferService;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OfferFacade {


    private final OfferFromUrlHandler offerFromUrlHandler;
    private final OfferApplicationHandler offerApplicationHandler;

    private final OfferAdditionHandler additionHandler;
    private final OfferUpdateHandler updateHandler;
    private final OfferDeletionHandler deletionHandler;
    private final OfferRetrievalHandler retrievalHandler;
    private final OfferNotAppliedHandler notAppliedHandler;
    private final OfferAppliedListHandler appliedHandler;

    private final OfferDetailsHandler detailsHandler;
    private final OfferCacheFacade offerCacheFacade;
    private final ExternalJobOfferService externalJobOfferService;
    private final OfferPushHandler pushHandler;

    private final ApplicationNoteHandler applicationNoteHandler;

    public OfferFacade(OfferFromUrlHandler offerFromUrlHandler,
                       OfferApplicationHandler offerApplicationHandler,
                       OfferAdditionHandler additionHandler,
                       OfferUpdateHandler updateHandler,
                       OfferDeletionHandler deletionHandler,
                       OfferRetrievalHandler retrievalHandler,
                       OfferNotAppliedHandler notAppliedHandler,
                       OfferAppliedListHandler appliedHandler,
                       OfferDetailsHandler detailsHandler,
                       OfferCacheFacade offerCacheFacade,
                       ExternalJobOfferService externalJobOfferService,
                       OfferPushHandler pushHandler,
                       ApplicationNoteHandler applicationNoteHandler) {
        this.offerFromUrlHandler = offerFromUrlHandler;
        this.offerApplicationHandler = offerApplicationHandler;
        this.additionHandler = additionHandler;
        this.updateHandler = updateHandler;
        this.deletionHandler = deletionHandler;
        this.retrievalHandler = retrievalHandler;
        this.notAppliedHandler = notAppliedHandler;
        this.appliedHandler = appliedHandler;
        this.detailsHandler = detailsHandler;
        this.offerCacheFacade = offerCacheFacade;
        this.externalJobOfferService = externalJobOfferService;
        this.pushHandler = pushHandler;
        this.applicationNoteHandler = applicationNoteHandler;
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

    public void pushOfferToProvider(String offerId, String providerName) {
        pushHandler.pushOfferToProvider(offerId, providerName);
    }

    public OfferDTO addOfferFromUrl(String offerUrl) {

        return offerFromUrlHandler.addOfferFromUrl(offerUrl);
    }

    public void applyToOffer(String offerId) {
        offerApplicationHandler.applyToOfferWithNote(offerId);
    }


}
