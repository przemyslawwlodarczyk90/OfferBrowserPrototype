package com.example.offerbrowserprototype.infrastructure.scraper;

import com.example.offerbrowserprototype.domain.offer.OfferSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

/** Per-portal scraper configuration (application-*.properties, prefix {@code scraper.*}). */
@Component
public class ScraperSettings {

    public record Portal(
            OfferSource source,
            String bulkScript,
            String offerScript,
            String outputFile,
            Duration bulkTimeout,
            Duration offerTimeout,
            boolean enabled,
            boolean scrapeOnSchedule
    ) {}

    private final Map<OfferSource, Portal> portals;

    @Autowired
    public ScraperSettings(
            @Value("${scraper.nofluff.script:${python.script.path:scripts/nofluff/scraper.py}}") String nfBulk,
            @Value("${scraper.nofluff.offer-script:${python.script.offer-url-scraper:scripts/nofluff/scrape_offer_from_url.py}}") String nfOffer,
            @Value("${scraper.nofluff.file:data/offers/detailed_offers.json}") String nfFile,
            @Value("${scraper.nofluff.timeout-minutes:120}") long nfTimeout,
            @Value("${scraper.nofluff.enabled:true}") boolean nfEnabled,
            @Value("${scraper.nofluff.scrape-on-schedule:true}") boolean nfScheduled,
            @Value("${scraper.theprotocol.script:scripts/theprotocol/scrape_theprotocol.py}") String tpBulk,
            @Value("${scraper.theprotocol.offer-script:scripts/theprotocol/scrape_theprotocol_offer.py}") String tpOffer,
            @Value("${scraper.theprotocol.file:data/offers/theprotocol_offers.json}") String tpFile,
            @Value("${scraper.theprotocol.timeout-minutes:50}") long tpTimeout,
            @Value("${scraper.theprotocol.enabled:true}") boolean tpEnabled,
            @Value("${scraper.theprotocol.scrape-on-schedule:true}") boolean tpScheduled,
            @Value("${scraper.justjoin.script:scripts/justjoin/scrape_justjoin.py}") String jjBulk,
            @Value("${scraper.justjoin.offer-script:scripts/justjoin/scrape_justjoin_offer.py}") String jjOffer,
            @Value("${scraper.justjoin.file:data/offers/justjoin_offers.json}") String jjFile,
            @Value("${scraper.justjoin.timeout-minutes:50}") long jjTimeout,
            @Value("${scraper.justjoin.enabled:true}") boolean jjEnabled,
            @Value("${scraper.justjoin.scrape-on-schedule:true}") boolean jjScheduled,
            @Value("${scraper.offer-timeout-seconds:120}") long offerTimeoutSeconds
    ) {
        Duration offerTimeout = Duration.ofSeconds(offerTimeoutSeconds);
        this.portals = Map.of(
                OfferSource.NOFLUFF, new Portal(OfferSource.NOFLUFF, nfBulk, nfOffer, nfFile,
                        Duration.ofMinutes(nfTimeout), offerTimeout, nfEnabled, nfScheduled),
                OfferSource.THEPROTOCOL, new Portal(OfferSource.THEPROTOCOL, tpBulk, tpOffer, tpFile,
                        Duration.ofMinutes(tpTimeout), offerTimeout, tpEnabled, tpScheduled),
                OfferSource.JUSTJOIN, new Portal(OfferSource.JUSTJOIN, jjBulk, jjOffer, jjFile,
                        Duration.ofMinutes(jjTimeout), offerTimeout, jjEnabled, jjScheduled)
        );
    }

    /** No Fluff Jobs + theprotocol.it only; Just Join IT gets its defaults (tests). */
    public ScraperSettings(String nfBulk, String nfOffer, String nfFile, long nfTimeout, boolean nfEnabled,
                           boolean nfScheduled, String tpBulk, String tpOffer, String tpFile, long tpTimeout,
                           boolean tpEnabled, boolean tpScheduled, long offerTimeoutSeconds) {
        this(nfBulk, nfOffer, nfFile, nfTimeout, nfEnabled, nfScheduled,
                tpBulk, tpOffer, tpFile, tpTimeout, tpEnabled, tpScheduled,
                "scripts/justjoin/scrape_justjoin.py", "scripts/justjoin/scrape_justjoin_offer.py",
                "data/offers/justjoin_offers.json", 50, true, true, offerTimeoutSeconds);
    }

    public Portal of(OfferSource source) {
        return portals.get(source);
    }
}
