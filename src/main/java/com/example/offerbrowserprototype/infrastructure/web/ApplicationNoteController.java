package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNoteFacade;
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

    public ApplicationNoteController(ApplicationNoteFacade applicationNoteFacade) {
        this.applicationNoteFacade = applicationNoteFacade;
    }

    @Operation(summary = "Get all application notes", description = "Retrieve all application notes from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved application notes")
    })
    @GetMapping
    public ResponseEntity<List<ApplicationNote>> getAllApplicationNotes() {
        logger.info("Fetching all application notes...");
        List<ApplicationNote> notes = applicationNoteFacade.getAllApplicationNotes();
        logger.info("Retrieved {} application notes.", notes.size());
        return ResponseEntity.ok(notes);
    }

    @Operation(summary = "Get application notes by company name", description = "Retrieve all application notes for a specific company.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved application notes"),
            @ApiResponse(responseCode = "404", description = "No application notes found for the given company name")
    })
    @GetMapping("/by-company-name")
    public ResponseEntity<List<ApplicationNote>> getApplicationNotesByCompanyName(@RequestParam String companyName) {
        logger.info("Fetching application notes for companyName: {}", companyName);
        List<ApplicationNote> notes = applicationNoteFacade.getApplicationNotesByCompanyName(companyName);
        if (notes.isEmpty()) {
            logger.warn("No application notes found for companyName: {}", companyName);
            return ResponseEntity.notFound().build();
        }
        logger.info("Retrieved {} application notes for companyName: {}", notes.size(), companyName);
        return ResponseEntity.ok(notes);
    }

    @Operation(summary = "Get companies with application dates", description = "Retrieve a list of companies with their application dates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved companies with application dates"),
            @ApiResponse(responseCode = "404", description = "No application notes found")
    })
    @GetMapping("/companies-with-dates")
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

    @Operation(summary = "Create a new application note for an external source", description = "Add a new note with company name and URL for applications done outside the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Note created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/external")
    public ResponseEntity<ApplicationNote> createNoteForExternalSource(
            @RequestParam String companyName,
            @RequestParam String url) {
        // Wywołanie handlera do stworzenia notatki
        ApplicationNote note = applicationNoteFacade.createNoteForExternalSource(companyName, url);
        return ResponseEntity.status(201).body(note);
    }
}
