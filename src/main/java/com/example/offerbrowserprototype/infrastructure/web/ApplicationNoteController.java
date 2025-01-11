package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
import com.example.offerbrowserprototype.infrastructure.facade.ApplicationNoteFacade;
import com.example.offerbrowserprototype.domain.dto.aplicationnote.ApplicationNoteDTO;
import com.example.offerbrowserprototype.domain.mapper.ApplicationNoteMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/application-notes")
@Tag(name = "Application Notes", description = "Operations related to application notes")
@PreAuthorize("isAuthenticated()")
public class ApplicationNoteController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationNoteController.class);
    private final ApplicationNoteFacade applicationNoteFacade;
    private final ApplicationNoteMapper applicationNoteMapper;

    public ApplicationNoteController(ApplicationNoteFacade applicationNoteFacade, ApplicationNoteMapper applicationNoteMapper) {
        this.applicationNoteFacade = applicationNoteFacade;
        this.applicationNoteMapper = applicationNoteMapper;
    }

    @GetMapping
    @Operation(summary = "Get all application notes", description = "Retrieve all application notes for the authenticated user.")
    public ResponseEntity<List<ApplicationNoteDTO>> getAllApplicationNotes(@RequestParam String userId) {
        logger.info("Fetching all application notes for userId: {}", userId);
        if (userId == null || userId.isBlank()) {
            logger.warn("User ID cannot be null or blank.");
            return ResponseEntity.badRequest().build();
        }

        List<ApplicationNoteDTO> notes = applicationNoteMapper.toDtoList(applicationNoteFacade.getAllApplicationNotes(userId.trim()));
        logger.info("Retrieved {} application notes for userId: {}", notes.size(), userId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/by-company-name")
    @Operation(summary = "Get application notes by company name", description = "Retrieve application notes for a specific company and user.")
    public ResponseEntity<List<ApplicationNoteDTO>> getApplicationNotesByCompanyName(
            @RequestParam String userId,
            @RequestParam String companyName) {
        logger.info("Fetching application notes for userId: {}, companyName: {}", userId, companyName);

        if (userId == null || userId.isBlank()) {
            logger.warn("User ID cannot be null or blank.");
            return ResponseEntity.badRequest().build();
        }
        if (companyName == null || companyName.isBlank()) {
            logger.warn("Company name cannot be null or blank.");
            return ResponseEntity.badRequest().build();
        }

        List<ApplicationNoteDTO> notes = applicationNoteMapper.toDtoList(
                applicationNoteFacade.getApplicationNotesByCompanyName(userId.trim(), companyName.trim())
        );
        if (notes.isEmpty()) {
            logger.warn("No application notes found for userId: {}, companyName: {}", userId, companyName);
            return ResponseEntity.notFound().build();
        }

        logger.info("Retrieved {} application notes for userId: {}, companyName: {}", notes.size(), userId, companyName);
        return ResponseEntity.ok(notes);
    }

    @PostMapping("/external")
    @Operation(summary = "Create a new application note for an external source", description = "Add a new note with company name, URL, and userId.")
    public ResponseEntity<ApplicationNoteDTO> createNoteForExternalSource(
            @RequestParam String userId,
            @RequestParam String companyName,
            @RequestParam String url) {
        logger.info("Creating a new application note for userId: {}, company: {}, URL: {}", userId, companyName, url);

        if (userId == null || userId.isBlank()) {
            logger.warn("User ID cannot be null or blank.");
            return ResponseEntity.badRequest().build();
        }
        if (companyName == null || companyName.isBlank() || url == null || url.isBlank()) {
            logger.warn("Company name and URL cannot be null or blank.");
            return ResponseEntity.badRequest().build();
        }

        ApplicationNoteDTO createdNote = applicationNoteMapper.toDto(
                applicationNoteFacade.createNoteForExternalSource(userId.trim(), companyName.trim(), url.trim())
        );
        return ResponseEntity.status(201).body(createdNote);
    }

    @GetMapping("/count")
    @Operation(summary = "Count all application notes", description = "Retrieve the total count of application notes for a specific user.")
    public ResponseEntity<Long> countAllApplicationNotes(@RequestParam String userId) {
        logger.info("Counting all application notes for userId: {}", userId);

        if (userId == null || userId.isBlank()) {
            logger.warn("User ID cannot be null or blank.");
            return ResponseEntity.badRequest().build();
        }

        long count = applicationNoteFacade.countAllApplicationNotes(userId.trim());
        logger.info("Total application notes count for userId {}: {}", userId, count);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/companies-with-dates")
    @Operation(summary = "Get companies with application dates", description = "Retrieve a list of companies with their application dates for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved companies with application dates"),
            @ApiResponse(responseCode = "404", description = "No application notes found")
    })
    public ResponseEntity<Map<String, List<String>>> getCompaniesWithApplicationDates(@RequestParam String userId) {
        logger.info("Fetching companies with application dates for userId: {}", userId);

        if (userId == null || userId.isBlank()) {
            logger.warn("User ID cannot be null or blank.");
            return ResponseEntity.badRequest().build();
        }

        Map<String, List<String>> companiesWithDates = applicationNoteFacade.getCompaniesWithApplicationDates(userId.trim());
        if (companiesWithDates.isEmpty()) {
            logger.warn("No application notes found for userId: {}", userId);
            return ResponseEntity.notFound().build();
        }

        logger.info("Retrieved {} companies with application dates for userId: {}", companiesWithDates.size(), userId);
        return ResponseEntity.ok(companiesWithDates);
    }
}
