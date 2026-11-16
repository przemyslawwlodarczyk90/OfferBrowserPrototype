package com.example.offerbrowserprototype.domain.scraperurl;

import com.example.offerbrowserprototype.domain.dto.scraperurl.ScraperSearchUrlDTO;
import com.example.offerbrowserprototype.domain.exception.ConflictException;
import com.example.offerbrowserprototype.domain.exception.NotFoundException;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.infrastructure.repository.ScraperSearchUrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Search URLs of each portal's scraper, edited by the admin. A portal always keeps
 * at least one URL — the scraper has nothing to walk through otherwise — so the
 * last one can only be replaced (add the new one, then delete the old one).
 */
@Component
public class ScraperSearchUrlHandler {

    private static final Logger logger = LoggerFactory.getLogger(ScraperSearchUrlHandler.class);

    private final ScraperSearchUrlRepository repository;
    private final Clock clock;

    public ScraperSearchUrlHandler(ScraperSearchUrlRepository repository, Clock clock) {
        this.repository = repository;
        // timestamps in the server's local time, like the rest of the application's LocalDateTime fields
        this.clock = clock.withZone(ZoneId.systemDefault());
    }

    @Transactional(readOnly = true)
    public List<ScraperSearchUrlDTO> list(OfferSource source) {
        Set<String> defaults = defaultUrls(source);
        return repository.findBySourceOrderByIdAsc(source.value()).stream()
                .map(e -> ScraperSearchUrlDTO.from(e, defaults.contains(e.getUrl())))
                .toList();
    }

    /** URLs passed to the scraper, in the order they were added. */
    @Transactional(readOnly = true)
    public List<String> urlsFor(OfferSource source) {
        return repository.findBySourceOrderByIdAsc(source.value()).stream().map(ScraperSearchUrl::getUrl).toList();
    }

    @Transactional
    public ScraperSearchUrlDTO add(OfferSource source, String url, String label, String username) {
        String normalized = validateUrl(source, url);
        if (repository.existsBySourceAndUrl(source.value(), normalized)) {
            throw new ConflictException("Ten adres jest już na liście " + source.displayName());
        }
        LocalDateTime now = LocalDateTime.now(clock);
        ScraperSearchUrl saved = repository.save(ScraperSearchUrl.builder()
                .source(source.value())
                .url(normalized)
                .label(cleanLabel(label))
                .createdAt(now)
                .createdBy(username)
                .build());
        logger.info("[{}] Search URL added by {}: {}", source.value(), username, normalized);
        return ScraperSearchUrlDTO.from(saved, defaultUrls(source).contains(normalized));
    }

    @Transactional
    public ScraperSearchUrlDTO update(Long id, String url, String label, String username) {
        ScraperSearchUrl entity = find(id);
        OfferSource source = OfferSource.fromParam(entity.getSource()).orElseThrow();
        String normalized = validateUrl(source, url);
        if (repository.existsBySourceAndUrlAndIdNot(source.value(), normalized, id)) {
            throw new ConflictException("Ten adres jest już na liście " + source.displayName());
        }
        logger.info("[{}] Search URL {} changed by {}: {} → {}", source.value(), id, username, entity.getUrl(), normalized);
        entity.setUrl(normalized);
        entity.setLabel(cleanLabel(label));
        entity.setUpdatedAt(LocalDateTime.now(clock));
        return ScraperSearchUrlDTO.from(repository.save(entity), defaultUrls(source).contains(normalized));
    }

    @Transactional
    public void delete(Long id, String username) {
        ScraperSearchUrl entity = find(id);
        if (repository.countBySource(entity.getSource()) <= 1) {
            throw new ConflictException("To ostatni adres wyszukiwania tego portalu — scraper nie miałby czego "
                    + "przeszukiwać. Dodaj nowy adres, a potem usuń ten.");
        }
        repository.delete(entity);
        logger.info("[{}] Search URL {} removed by {}: {}", entity.getSource(), id, username, entity.getUrl());
    }

    /** Adds the built-in URLs that are missing; existing entries stay untouched. @return number added */
    @Transactional
    public int restoreDefaults(OfferSource source, String username) {
        Set<String> present = Set.copyOf(urlsFor(source));
        LocalDateTime now = LocalDateTime.now(clock);
        int added = 0;
        for (DefaultSearchUrls.Entry d : DefaultSearchUrls.of(source)) {
            if (!present.contains(d.url())) {
                repository.save(ScraperSearchUrl.builder().source(source.value()).url(d.url()).label(d.label())
                        .createdAt(now).createdBy(username).build());
                added++;
            }
        }
        if (added > 0) {
            logger.info("[{}] {} default search URL(s) restored by {}", source.value(), added,
                    username == null ? "startup" : username);
        }
        return added;
    }

    /** Startup: a portal without any URL gets the built-in list. */
    @Transactional
    public void seedMissingPortals() {
        for (OfferSource source : OfferSource.values()) {
            if (repository.countBySource(source.value()) == 0) {
                restoreDefaults(source, null);
            }
        }
    }

    private ScraperSearchUrl find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie ma adresu wyszukiwania o id " + id));
    }

    /** https/http URL on the portal's domain; trimmed. */
    static String validateUrl(OfferSource source, String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Podaj adres wyszukiwania");
        }
        String trimmed = url.trim();
        if (trimmed.length() > ScraperSearchUrl.MAX_URL_LENGTH) {
            throw new IllegalArgumentException("Adres jest za długi (maks. " + ScraperSearchUrl.MAX_URL_LENGTH + " znaków)");
        }
        if (trimmed.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("Adres nie może zawierać spacji");
        }
        URI uri;
        try {
            uri = URI.create(trimmed);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Niepoprawny adres URL: " + e.getMessage());
        }
        if (!"https".equalsIgnoreCase(uri.getScheme()) && !"http".equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalArgumentException("Adres musi zaczynać się od https://");
        }
        if (!source.matchesUrl(trimmed)) {
            throw new IllegalArgumentException("Adres musi prowadzić do " + source.domain()
                    + " — to lista portalu " + source.displayName());
        }
        return trimmed;
    }

    private static String cleanLabel(String label) {
        if (label == null || label.isBlank()) {
            return null;
        }
        String l = label.trim();
        return l.length() > ScraperSearchUrl.MAX_LABEL_LENGTH ? l.substring(0, ScraperSearchUrl.MAX_LABEL_LENGTH) : l;
    }

    private static Set<String> defaultUrls(OfferSource source) {
        return DefaultSearchUrls.of(source).stream().map(DefaultSearchUrls.Entry::url).collect(Collectors.toSet());
    }
}
