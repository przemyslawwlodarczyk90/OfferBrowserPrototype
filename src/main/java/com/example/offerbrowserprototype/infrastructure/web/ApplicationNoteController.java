package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNoteFacade;
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
    public ResponseEntity<List<ApplicationNoteDTO>> getAllApplicationNotes() {
        logger.info("Fetching all application notes...");
        List<ApplicationNote> notes = applicationNoteFacade.getAllApplicationNotes();
        return ResponseEntity.ok(applicationNoteMapper.toDtoList(notes));
    }

    @GetMapping("/by-company-name")
    public ResponseEntity<List<ApplicationNoteDTO>> getApplicationNotesByCompanyName(@RequestParam String companyName) {
        logger.info("Fetching application notes for companyName: {}", companyName);
        List<ApplicationNote> notes = applicationNoteFacade.getApplicationNotesByCompanyName(companyName);
        if (notes.isEmpty()) {
            logger.warn("No application notes found for companyName: {}", companyName);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(applicationNoteMapper.toDtoList(notes));
    }

    @PostMapping("/external")
    public ResponseEntity<ApplicationNoteDTO> createNoteForExternalSource(@RequestParam String companyName, @RequestParam String url) {
        ApplicationNote note = applicationNoteFacade.createNoteForExternalSource(companyName, url);
        return ResponseEntity.status(201).body(applicationNoteMapper.toDto(note));
    }

    @GetMapping("/count")
    @Operation(summary = "Count all application notes", description = "Retrieve the total count of application notes in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the count")
    })
    public ResponseEntity<Long> countAllApplicationNotes() {
        long count = applicationNoteFacade.countAllApplicationNotes();
        return ResponseEntity.ok(count);
    }
}
