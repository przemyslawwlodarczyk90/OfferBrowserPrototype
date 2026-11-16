package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNoteHandler;
import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.domain.requirement.NiceToHaveCreationHandler;
import com.example.offerbrowserprototype.domain.requirement.RequirementCreationHandler;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferStatus;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperJobService;
import com.example.offerbrowserprototype.infrastructure.scraper.ScraperSettings;
import com.example.offerbrowserprototype.infrastructure.scraper.ScriptLocator;
import com.example.offerbrowserprototype.infrastructure.service.PythonProcessRunner;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Imports one offer from a URL pasted by the user. The scraper is chosen from
 * the URL's verified domain; unknown domains are rejected before any request.
 */
@Component
public class OfferFromUrlHandler {

    private static final Logger logger = LoggerFactory.getLogger(OfferFromUrlHandler.class);
    private static final ObjectMapper JSON = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final ApplicationNoteHandler applicationNoteHandler;
    private final UserOfferStatusRepository userOfferStatusRepository;
    private final UserRepository userRepository;
    private final RequirementCreationHandler requirementCreationHandler;
    private final NiceToHaveCreationHandler niceToHaveCreationHandler;
    private final PythonProcessRunner runner;
    private final ScraperSettings settings;
    private final ScriptLocator scriptLocator;

    public OfferFromUrlHandler(
            OfferRepository offerRepository,
            OfferMapper offerMapper,
            ApplicationNoteHandler applicationNoteHandler,
            UserOfferStatusRepository userOfferStatusRepository,
            UserRepository userRepository,
            RequirementCreationHandler requirementCreationHandler,
            NiceToHaveCreationHandler niceToHaveCreationHandler,
            PythonProcessRunner runner,
            ScraperSettings settings,
            ScriptLocator scriptLocator
    ) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.applicationNoteHandler = applicationNoteHandler;
        this.userOfferStatusRepository = userOfferStatusRepository;
        this.userRepository = userRepository;
        this.requirementCreationHandler = requirementCreationHandler;
        this.niceToHaveCreationHandler = niceToHaveCreationHandler;
        this.runner = runner;
        this.settings = settings;
        this.scriptLocator = scriptLocator;
    }

    /**
     * @throws IllegalArgumentException unsupported/invalid URL or scraping error
     * @throws IllegalStateException    the offer is already in the database
     */
    public OfferDTO addOfferFromUrl(Long userId, String offerUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        OfferSource source = OfferSource.fromUrl(offerUrl).orElseThrow(() -> new IllegalArgumentException(
                "Nieobsługiwany adres — obsługiwane portale: " + OfferSource.supportedDomains()));
        ScraperSettings.Portal portal = settings.of(source);
        if (!portal.enabled()) {
            throw new ScraperJobService.ScraperDisabledException("Pobieranie z " + source.displayName() + " jest wyłączone");
        }
        if (offerRepository.existsByOfferUrl(offerUrl.trim())) {
            throw new IllegalStateException("Oferta już istnieje w bazie: " + offerUrl);
        }

        OfferDTO scrapedOffer = scrapeOffer(source, portal, offerUrl.trim());
        if (offerRepository.existsByOfferUrl(scrapedOffer.getOfferUrl())) {
            throw new IllegalStateException("Oferta już istnieje w bazie: " + scrapedOffer.getOfferUrl());
        }

        Offer offer = offerMapper.toEntity(scrapedOffer);
        offer.setSource(source.value());
        if (offer.getFetchedAt() == null) {
            offer.setFetchedAt(LocalDateTime.now());
        }
        offer = offerRepository.save(offer);
        requirementCreationHandler.createFromOffer(offer);
        niceToHaveCreationHandler.createFromOffer(offer);

        UserOfferStatus status = new UserOfferStatus(user, offer, false);
        status.setAppliedAt(LocalDateTime.now());
        userOfferStatusRepository.save(status);

        applicationNoteHandler.saveApplicationNote(
                userId,
                offer.getId(),
                offer.getOfferUrl(),
                offer.getCompany()
        );

        return offerMapper.toDTO(offer);
    }

    private OfferDTO scrapeOffer(OfferSource source, ScraperSettings.Portal portal, String offerUrl) {
        PythonProcessRunner.Result result = runner.run(
                scriptLocator.resolve(portal.offerScript()), List.of(offerUrl), portal.offerTimeout());
        if (result.timedOut()) {
            throw new IllegalArgumentException("Przekroczono czas pobierania oferty (" + portal.offerTimeout().toSeconds() + " s)");
        }
        if (result.exitCode() != 0) {
            logger.warn("[{}] Offer scraper failed (exit {}): {}", source.value(), result.exitCode(), result.stderrTail());
            throw new IllegalArgumentException(ScraperJobService.describeExit(result.exitCode()));
        }
        try {
            OfferDTO dto = JSON.readValue(result.lastStdoutLine(), OfferDTO.class);
            String rejection = OfferValidation.rejectionReason(dto.getOfferUrl(), dto.getTitle(), dto.getCompany(), source);
            if (rejection != null) {
                logger.warn("[{}] Scraped offer rejected ({}): {}", source.value(), rejection, offerUrl);
                throw new IllegalArgumentException("Nie zapisano oferty — " + rejection
                        + " (strona mogła nie zostać poprawnie wczytana)");
            }
            return dto;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Cannot parse scraper output", e);
            throw new IllegalArgumentException("Niepoprawna odpowiedź scrapera: " + e.getMessage(), e);
        }
    }
}
