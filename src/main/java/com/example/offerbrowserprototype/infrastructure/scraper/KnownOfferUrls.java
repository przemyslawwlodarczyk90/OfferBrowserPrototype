package com.example.offerbrowserprototype.infrastructure.scraper;

import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Writes the URLs of a portal's offers already stored in PostgreSQL to a temporary
 * JSON file passed to the scraper ({@code --known-urls}), so it never opens their
 * detail pages again. Java stays the only component talking to the database.
 */
@Component
public class KnownOfferUrls {

    private static final ObjectMapper JSON = new ObjectMapper();
    /** Portals whose bulk script understands {@code --known-urls}. */
    private static final Set<OfferSource> SUPPORTED = Set.of(OfferSource.NOFLUFF, OfferSource.JUSTJOIN);

    private final OfferRepository offerRepository;

    public KnownOfferUrls(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public boolean supports(OfferSource source) {
        return SUPPORTED.contains(source);
    }

    /**
     * @return path of the temporary file (caller deletes it)
     * @throws IllegalStateException when the database cannot be read — the run must not
     *                               continue as if it were empty
     */
    public Path writeTempFile(OfferSource source) {
        List<String> urls;
        try {
            urls = offerRepository.findOfferUrlsBySource(source.value(), "https://" + source.domain() + "/%");
        } catch (RuntimeException e) {
            throw new IllegalStateException("Nie można odczytać istniejących ofert z bazy — pobieranie przerwane: "
                    + e.getMessage(), e);
        }
        try {
            Path file = Files.createTempFile("known-" + source.value().toLowerCase() + "-", ".json");
            JSON.writeValue(file.toFile(), urls);
            return file;
        } catch (IOException e) {
            throw new IllegalStateException("Nie można zapisać listy istniejących ofert: " + e.getMessage(), e);
        }
    }
}
