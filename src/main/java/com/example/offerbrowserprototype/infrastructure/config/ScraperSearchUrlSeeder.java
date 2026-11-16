package com.example.offerbrowserprototype.infrastructure.config;

import com.example.offerbrowserprototype.domain.scraperurl.ScraperSearchUrlHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/** Fills the search URL list of every portal that has none (first start, new portal). */
@Component
public class ScraperSearchUrlSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ScraperSearchUrlSeeder.class);

    private final ScraperSearchUrlHandler handler;

    public ScraperSearchUrlSeeder(ScraperSearchUrlHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            handler.seedMissingPortals();
        } catch (RuntimeException e) {
            // the application still starts; the scraper reports the empty list on its next run
            log.error("Could not seed default scraper search URLs: {}", e.getMessage());
        }
    }
}
