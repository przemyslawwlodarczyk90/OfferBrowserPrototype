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
    @Operation(summary = "Get all application notes", description = "Retrieve all application notes.")
    public ResponseEntity<List<ApplicationNoteDTO>> getAllApplicationNotes() {
        logger.info("Fetching all application notes...");
        List<ApplicationNoteDTO> notes = applicationNoteMapper.toDtoList(applicationNoteFacade.getAllApplicationNotes());
        logger.info("Retrieved {} application notes.", notes.size());
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/by-company-name")
    @Operation(summary = "Get application notes by company name", description = "Retrieve application notes for a specific company.")
    public ResponseEntity<List<ApplicationNoteDTO>> getApplicationNotesByCompanyName(@RequestParam String companyName) {
        logger.info("Fetching application notes for companyName: {}", companyName);
        if (companyName == null || companyName.isBlank()) {
            logger.warn("Company name cannot be null or blank.");
            return ResponseEntity.badRequest().build();
        }
        List<ApplicationNoteDTO> notes = applicationNoteMapper.toDtoList(
                applicationNoteFacade.getApplicationNotesByCompanyName(companyName.trim())
        );
        if (notes.isEmpty()) {
            logger.warn("No application notes found for companyName: {}", companyName);
            return ResponseEntity.notFound().build();
        }
        logger.info("Retrieved {} application notes for companyName: {}", notes.size(), companyName);
        return ResponseEntity.ok(notes);
    }

    @PostMapping("/external")
    @Operation(summary = "Create a new application note for an external source", description = "Add a new note with company name and URL.")
    public ResponseEntity<ApplicationNoteDTO> createNoteForExternalSource(@RequestParam String companyName, @RequestParam String url) {
        logger.info("Creating a new application note for company: {}, URL: {}", companyName, url);
        ApplicationNoteDTO createdNote = applicationNoteMapper.toDto(
                applicationNoteFacade.createNoteForExternalSource(companyName, url)
        );
        return ResponseEntity.status(201).body(createdNote);
    }

    @GetMapping("/count")
    @Operation(summary = "Count all application notes", description = "Retrieve the total count of application notes.")
    public ResponseEntity<Long> countAllApplicationNotes() {
        long count = applicationNoteFacade.countAllApplicationNotes();
        logger.info("Total application notes count: {}", count);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/companies-with-dates")
    @Operation(summary = "Get companies with application dates", description = "Retrieve a list of companies with their application dates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved companies with application dates"),
            @ApiResponse(responseCode = "404", description = "No application notes found")
    })
    public ResponseEntity<Map<String, List<String>>> getCompaniesWithApplicationDates() {
        logger.info("Fetching companies with application dates...");
        Map<String, List<String>> companiesWithDates = applicationNoteFacade.getCompaniesWithApplicationDates();
        if (companiesWithDates.isEmpty()) {
            logger.warn("No application notes found.");
            return ResponseEntity.notFound().build();
        }
        logger.info("Retrieved {} companies with application dates.", companiesWithDates.size());
        return ResponseEntity.ok(companiesWithDates);
    }
}
