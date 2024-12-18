package com.example.offerbrowserprototype.infrastructure.scheduler;


import com.example.offerbrowserprototype.infrastructure.service.OfferImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OfferScheduler {

    @Autowired
    private OfferImportService offerImportService;

    @Scheduled(cron = "0 0 6,12,18,23 * * *")
    public void importOffers() {
        offerImportService.importOffersFromJson("data/offers/detailed_offers.json");
    }
}
