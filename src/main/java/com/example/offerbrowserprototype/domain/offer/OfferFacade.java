package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheFacade;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.service.ExternalJobOfferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OfferFacade {

    private static final Logger logger = LoggerFactory.getLogger(OfferFacade.class);

    private final OfferMapper offerMapper;
    private final OfferRepository offerRepository;

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

    public OfferFacade(OfferMapper offerMapper,
                       OfferRepository offerRepository,
                       OfferAdditionHandler additionHandler,
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
        this.offerMapper = offerMapper;
        this.offerRepository = offerRepository;
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

        if (offerDto == null) {
            throw new IllegalArgumentException("Failed to scrape offer details from URL: " + offerUrl);
        }

        // Zapis oferty do bazy
        Offer offer = offerMapper.toEntity(offerDto);
        offer = offerRepository.save(offer);

        // Logowanie zapisanej oferty
        logger.info("Saved offer with ID: {}", offer.getId());

        // Mapowanie na DTO z zapisanym ID
        return offerMapper.toDTO(offer);
    }


    public void applyToOffer(String offerId) {
        // Pobierz szczegóły oferty
        OfferDTO offer = detailsHandler.getOfferById(offerId);

        if (offer == null) {
            throw new IllegalArgumentException("Offer not found for ID: " + offerId);
        }

        // Loguj szczegóły oferty
        System.out.println("Offer Details:");
        System.out.println("ID: " + offer.getId());
        System.out.println("URL: " + offer.getOfferUrl());
        System.out.println("Company: " + offer.getCompany());

        // Oznacz ofertę jako aplikowaną
        applicationHandler.applyToOffer(offerId);

        // Zapis notatki aplikacji
        if (offer.getOfferUrl() == null || offer.getCompany() == null) {
            throw new IllegalArgumentException("Offer details are incomplete. Cannot save application note.");
        }

        saveApplicationNote(offerId, offer.getOfferUrl(), offer.getCompany());
    }

    public void saveApplicationNote(String offerId, String offerUrl, String companyName) {
        applicationNoteHandler.saveApplicationNote(offerId, offerUrl, companyName);
    }
}
