package com.example.offerbrowserprototype.infrastructure.scraper;

import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.domain.scraperurl.ScraperSearchUrlHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Writes a portal's search URLs (admin panel, database) to a temporary JSON file
 * passed to its bulk scraper as {@code --urls-file}.
 */
@Component
public class SearchUrlsFile {

    private static final ObjectMapper JSON = new ObjectMapper();

    private final ScraperSearchUrlHandler searchUrls;

    public SearchUrlsFile(ScraperSearchUrlHandler searchUrls) {
        this.searchUrls = searchUrls;
    }

    /**
     * @return path of the temporary file (caller deletes it)
     * @throws IllegalStateException when the list cannot be read or is empty — the scraper is not started
     */
    public Path writeTempFile(OfferSource source) {
        List<String> urls;
        try {
            urls = searchUrls.urlsFor(source);
        } catch (RuntimeException e) {
            throw new IllegalStateException("Nie można odczytać adresów wyszukiwania z bazy — pobieranie przerwane: "
                    + e.getMessage(), e);
        }
        if (urls.isEmpty()) {
            throw new IllegalStateException("Brak adresów wyszukiwania dla " + source.displayName()
                    + " — dodaj je w panelu administratora");
        }
        try {
            Path file = Files.createTempFile("search-urls-" + source.value().toLowerCase() + "-", ".json");
            JSON.writeValue(file.toFile(), urls);
            return file;
        } catch (IOException e) {
            throw new IllegalStateException("Nie można zapisać listy adresów wyszukiwania: " + e.getMessage(), e);
        }
    }
}
