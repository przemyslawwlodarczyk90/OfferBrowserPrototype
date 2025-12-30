package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.aplicationnote.ApplicationNoteDTO;
import com.example.offerbrowserprototype.domain.mapper.ApplicationNoteMapper;
import com.example.offerbrowserprototype.infrastructure.facade.ApplicationNoteFacade;
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

    public ApplicationNoteController(
            ApplicationNoteFacade applicationNoteFacade,
            ApplicationNoteMapper applicationNoteMapper
    ) {
        this.applicationNoteFacade = applicationNoteFacade;
        this.applicationNoteMapper = applicationNoteMapper;
    }

    @GetMapping
    @Operation(summary = "Get all application notes")
    public ResponseEntity<List<ApplicationNoteDTO>> getAllApplicationNotes(
            @RequestParam Long userId
    ) {
        logger.info("Fetching all application notes for userId={}", userId);

        List<ApplicationNoteDTO> notes = applicationNoteMapper.toDtoList(
                applicationNoteFacade.getAllApplicationNotes(userId)
        );

        return ResponseEntity.ok(notes);
    }

    @GetMapping("/by-company-name")
    @Operation(summary = "Get application notes by company name")
    public ResponseEntity<List<ApplicationNoteDTO>> getApplicationNotesByCompanyName(
            @RequestParam Long userId,
            @RequestParam String companyName
    ) {
        logger.info("Fetching application notes for userId={}, company={}", userId, companyName);

        if (companyName == null || companyName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        List<ApplicationNoteDTO> notes = applicationNoteMapper.toDtoList(
                applicationNoteFacade.getApplicationNotesByCompanyName(userId, companyName)
        );

        return notes.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(notes);
    }

    @PostMapping("/external")
    @Operation(summary = "Create application note for external source")
    public ResponseEntity<ApplicationNoteDTO> createNoteForExternalSource(
            @RequestParam Long userId,
            @RequestParam String companyName,
            @RequestParam String offerUrl
    ) {
        logger.info("Creating external application note for userId={}, company={}, url={}",
                userId, companyName, offerUrl);

        if (companyName == null || companyName.isBlank()
                || offerUrl == null || offerUrl.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        ApplicationNoteDTO created = applicationNoteMapper.toDto(
                applicationNoteFacade.createNoteForExternalSource(
                        userId,
                        companyName,
                        offerUrl
                )
        );

        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/count")
    @Operation(summary = "Count application notes")
    public ResponseEntity<Long> countAllApplicationNotes(
            @RequestParam Long userId
    ) {
        logger.info("Counting application notes for userId={}", userId);

        long count = applicationNoteFacade.countAllApplicationNotes(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/companies-with-dates")
    @Operation(summary = "Get companies with application dates")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Data retrieved"),
            @ApiResponse(responseCode = "404", description = "No data found")
    })
    public ResponseEntity<Map<String, List<String>>> getCompaniesWithApplicationDates(
            @RequestParam Long userId
    ) {
        logger.info("Fetching companies with application dates for userId={}", userId);

        Map<String, List<String>> result =
                applicationNoteFacade.getCompaniesWithApplicationDates(userId);

        return result.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(result);
    }
}
