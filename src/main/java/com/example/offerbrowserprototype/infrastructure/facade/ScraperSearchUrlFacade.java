package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.dto.scraperurl.ScraperSearchUrlDTO;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.domain.scraperurl.DefaultSearchUrls;
import com.example.offerbrowserprototype.domain.scraperurl.ScraperSearchUrlHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/** Admin management of the scrapers' search URLs (one list per portal). */
@Component
public class ScraperSearchUrlFacade {

    private final ScraperSearchUrlHandler handler;

    public ScraperSearchUrlFacade(ScraperSearchUrlHandler handler) {
        this.handler = handler;
    }

    public List<ScraperSearchUrlDTO> list(OfferSource source) {
        return handler.list(source);
    }

    public List<DefaultSearchUrls.Entry> defaults(OfferSource source) {
        return DefaultSearchUrls.of(source);
    }

    public ScraperSearchUrlDTO add(OfferSource source, String url, String label, String username) {
        return handler.add(source, url, label, username);
    }

    public ScraperSearchUrlDTO update(Long id, String url, String label, String username) {
        return handler.update(id, url, label, username);
    }

    public void delete(Long id, String username) {
        handler.delete(id, username);
    }

    public int restoreDefaults(OfferSource source, String username) {
        return handler.restoreDefaults(source, username);
    }
}
