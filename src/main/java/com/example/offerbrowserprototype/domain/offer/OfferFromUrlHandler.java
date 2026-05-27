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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OfferFromUrlHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(OfferFromUrlHandler.class);

    @Value("${python.path}")
    private String pythonPath;

    @Value("${python.script.offer-url-scraper}")
    private String scriptPath;

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final ApplicationNoteHandler applicationNoteHandler;
    private final UserOfferStatusRepository userOfferStatusRepository;
    private final UserRepository userRepository;
    private final RequirementCreationHandler requirementCreationHandler;
    private final NiceToHaveCreationHandler niceToHaveCreationHandler;

    public OfferFromUrlHandler(
            OfferRepository offerRepository,
            OfferMapper offerMapper,
            ApplicationNoteHandler applicationNoteHandler,
            UserOfferStatusRepository userOfferStatusRepository,
            UserRepository userRepository,
            RequirementCreationHandler requirementCreationHandler,
            NiceToHaveCreationHandler niceToHaveCreationHandler
    ) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.applicationNoteHandler = applicationNoteHandler;
        this.userOfferStatusRepository = userOfferStatusRepository;
        this.userRepository = userRepository;
        this.requirementCreationHandler = requirementCreationHandler;
        this.niceToHaveCreationHandler = niceToHaveCreationHandler;
    }

    public OfferDTO addOfferFromUrl(Long userId, String offerUrl) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found: " + userId));

        OfferDTO scrapedOffer = scrapeOffer(offerUrl);

        Offer offer = offerMapper.toEntity(scrapedOffer);
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

    private OfferDTO scrapeOffer(String offerUrl) {
        try {
            Process process = new ProcessBuilder(
                    pythonPath, scriptPath, offerUrl
            ).start();

            String output;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                output = reader.lines().collect(Collectors.joining("\n"));
            }

            int exit = process.waitFor();
            if (exit != 0) {
                throw new RuntimeException("Python script failed");
            }

            String json = output.split("\n")[output.split("\n").length - 1];
            return new ObjectMapper().readValue(json, OfferDTO.class);

        } catch (Exception e) {
            logger.error("Error scraping offer", e);
            throw new RuntimeException("Scraping failed", e);
        }
    }
}
