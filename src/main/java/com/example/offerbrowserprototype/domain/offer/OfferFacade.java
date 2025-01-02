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
    private final OfferNotAppliedHandler notAppliedHandler;
    private final OfferAppliedHandler appliedHandler;
    private final OfferApplicationHandler applicationHandler;
    private final OfferDetailsHandler detailsHandler;
    private final OfferCacheFacade offerCacheFacade;
    private final ExternalJobOfferService externalJobOfferService;
    private final OfferPushHandler pushHandler;
    private final OfferFromUrlHandler offerFromUrlHandler;
    private final ApplicationNoteHandler applicationNoteHandler;

    public OfferFacade(OfferAdditionHandler additionHandler,
                       OfferUpdateHandler updateHandler,
                       OfferDeletionHandler deletionHandler,
                       OfferRetrievalHandler retrievalHandler,
                       OfferNotAppliedHandler notAppliedHandler,
                       OfferAppliedHandler appliedHandler,
                       OfferApplicationHandler applicationHandler,
                       OfferDetailsHandler detailsHandler,
                       OfferCacheFacade offerCacheFacade,
                       ExternalJobOfferService externalJobOfferService,
                       OfferPushHandler pushHandler,
                       OfferFromUrlHandler offerFromUrlHandler,
                       ApplicationNoteHandler applicationNoteHandler) {
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
        this.pushHandler = pushHandler;
        this.offerFromUrlHandler = offerFromUrlHandler;
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
        OfferDTO offerDto = offerFromUrlHandler.handleOfferFromUrl(offerUrl);
        return offerDto;
    }

    public void applyToOffer(String offerId) {

        OfferDTO offer = detailsHandler.getOfferById(offerId);

        if (offer == null) {
            throw new IllegalArgumentException("Offer not found for ID: " + offerId);
        }

        applicationHandler.applyToOffer(offerId);

        saveApplicationNote(offerId, offer.getOfferUrl(), offer.getCompany());
    }

    public void saveApplicationNote(String offerId, String offerUrl, String companyName) {
        applicationNoteHandler.saveApplicationNote(offerId, offerUrl, companyName);
    }
}
