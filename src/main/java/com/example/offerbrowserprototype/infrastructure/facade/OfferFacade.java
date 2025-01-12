package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNoteHandler;
import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.*;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheFacade;
import com.example.offerbrowserprototype.infrastructure.service.ExternalJobOfferService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OfferFacade {

    private final OfferFromUrlHandler offerFromUrlHandler;
    private final OfferDetailsHandler  detailsHandler;
    private final OfferAdditionHandler additionHandler;
    private final OfferUpdateHandler updateHandler;
    private final OfferDeletionHandler deletionHandler;
    private final OfferRetrievalHandler retrievalHandler;

    private final OfferCacheFacade offerCacheFacade;

    private final ExternalJobOfferService externalJobOfferService;
    private final OfferPushHandler pushHandler;

    private final MarkAsDuplicateHandler markAsDuplicateHandler;

    public OfferFacade(OfferFromUrlHandler offerFromUrlHandler,

                       OfferAdditionHandler additionHandler,
                       OfferUpdateHandler updateHandler,
                       OfferDeletionHandler deletionHandler,
                       OfferRetrievalHandler retrievalHandler,
                       OfferDetailsHandler detailsHandler,
                       OfferCacheFacade offerCacheFacade,
                       ExternalJobOfferService externalJobOfferService,
                       OfferPushHandler pushHandler,
                       ApplicationNoteHandler applicationNoteHandler,
                       MarkAsDuplicateHandler markAsDuplicateHandler) {
        this.offerFromUrlHandler = offerFromUrlHandler;
        this.additionHandler = additionHandler;
        this.updateHandler = updateHandler;
        this.deletionHandler = deletionHandler;
        this.retrievalHandler = retrievalHandler;
        this.detailsHandler = detailsHandler;
        this.offerCacheFacade = offerCacheFacade;
        this.externalJobOfferService = externalJobOfferService;
        this.pushHandler = pushHandler;
        this.markAsDuplicateHandler = markAsDuplicateHandler;
    }

    @CacheEvict(value = "offers", allEntries = true)
    public OfferDTO addOffer(OfferDTO offerDto) {
        return additionHandler.addOffer(offerDto);
    }

    @CacheEvict(value = "offers", allEntries = true)
    public OfferDTO updateOffer(String id, OfferDTO offerDto) {
        return updateHandler.updateOffer(id, offerDto);
    }

    @Cacheable(value = "offerDetails", key = "#id", unless = "#result == null")
    public OfferDTO getOffer(String id) {
        return detailsHandler.getOfferById(id);
    }

    @CacheEvict(value = "offers", allEntries = true)
    public void deleteOffer(String id) {
        deletionHandler.deleteOffer(id);
    }



    @Cacheable(value = "allOffers", unless = "#result.isEmpty()")
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

    public OfferDTO addOfferFromUrl(String userId, String offerUrl) {
        return offerFromUrlHandler.addOfferFromUrl(userId, offerUrl);
    }




    @CacheEvict(value = {"allOffers", "offerDetails"}, allEntries = true)
    public void markAsDuplicateById(String offerId) {
        markAsDuplicateHandler.handleById(offerId);
    }

    @CacheEvict(value = {"allOffers", "offerDetails"}, allEntries = true)
    public void markAsDuplicateByUrl(String offerUrl) {
        markAsDuplicateHandler.handleByUrl(offerUrl);
    }
}
