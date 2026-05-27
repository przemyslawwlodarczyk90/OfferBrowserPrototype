package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.analytics.SkillCountDTO;
import com.example.offerbrowserprototype.infrastructure.facade.RequirementAnalyticsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/analytics/requirements")
@Tag(name = "Requirements Analytics", description = "Market requirements analytics — most popular skills from job offers.")
@PreAuthorize("isAuthenticated()")
public class RequirementAnalyticsController {

    private final RequirementAnalyticsFacade requirementAnalyticsFacade;

    public RequirementAnalyticsController(RequirementAnalyticsFacade requirementAnalyticsFacade) {
        this.requirementAnalyticsFacade = requirementAnalyticsFacade;
    }

    @GetMapping("/top")
    @Operation(summary = "All skills overall",
               description = "Returns all required skills sorted by occurrence count. " +
                             "Pass ?sources=NoFluff,Pracuj to filter by source; omit for all data.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Skill list returned"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<List<SkillCountDTO>> getTopSkills(
            @Parameter(description = "Comma-separated source names to filter by (e.g. NoFluff,Pracuj). Omit to include all sources.")
            @RequestParam(required = false) String sources
    ) {
        return ResponseEntity.ok(requirementAnalyticsFacade.getTopSkills(parseSources(sources)));
    }

    @GetMapping("/top-by-level")
    @Operation(summary = "Skills by seniority level",
               description = "Returns all required skills for a given seniority level sorted by occurrence count. " +
                             "Use 'Nieokreślony poziom' to query offers with no level set.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Skill list returned"),
            @ApiResponse(responseCode = "400", description = "Missing required parameter: level"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<List<SkillCountDTO>> getTopSkillsByLevel(
            @Parameter(description = "Seniority level (e.g. Junior, Mid, Senior, Trainee, Nieokreślony poziom)", required = true)
            @RequestParam String level,
            @Parameter(description = "Comma-separated source names to filter by. Omit to include all sources.")
            @RequestParam(required = false) String sources
    ) {
        return ResponseEntity.ok(requirementAnalyticsFacade.getTopSkillsByLevel(level, parseSources(sources)));
    }

    @GetMapping("/levels")
    @Operation(summary = "Available seniority levels",
               description = "Returns distinct seniority levels present in the requirements fact table. " +
                             "Includes 'Nieokreślony poziom' if there are requirements with no level assigned.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Level list returned"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<List<String>> getAvailableLevels() {
        return ResponseEntity.ok(requirementAnalyticsFacade.getAvailableLevels());
    }

    @GetMapping("/sources")
    @Operation(summary = "Available data sources",
               description = "Returns distinct source names from the job_offers table (e.g. NoFluff). " +
                             "Used to populate the source filter on the analytics page.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Source list returned"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<List<String>> getAvailableSources() {
        return ResponseEntity.ok(requirementAnalyticsFacade.getAvailableSources());
    }

    private List<String> parseSources(String sources) {
        if (sources == null || sources.isBlank()) return Collections.emptyList();
        return Arrays.asList(sources.split(","));
    }
}
