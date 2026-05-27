package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.analytics.SkillCountDTO;
import com.example.offerbrowserprototype.infrastructure.facade.RequirementAnalyticsFacade;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "All skills overall", description = "Returns all required skills sorted by occurrence count. Optional: ?sources=NoFluff,Pracuj")
    public ResponseEntity<List<SkillCountDTO>> getTopSkills(
            @RequestParam(required = false) String sources
    ) {
        return ResponseEntity.ok(requirementAnalyticsFacade.getTopSkills(parseSources(sources)));
    }

    @GetMapping("/top-by-level")
    @Operation(summary = "All skills by seniority level", description = "Returns all required skills for a given level sorted by occurrence count. Optional: ?sources=NoFluff")
    public ResponseEntity<List<SkillCountDTO>> getTopSkillsByLevel(
            @RequestParam String level,
            @RequestParam(required = false) String sources
    ) {
        return ResponseEntity.ok(requirementAnalyticsFacade.getTopSkillsByLevel(level, parseSources(sources)));
    }

    @GetMapping("/levels")
    @Operation(summary = "Available levels", description = "Returns distinct seniority levels present in requirements data.")
    public ResponseEntity<List<String>> getAvailableLevels() {
        return ResponseEntity.ok(requirementAnalyticsFacade.getAvailableLevels());
    }

    @GetMapping("/sources")
    @Operation(summary = "Available sources", description = "Returns distinct source names present in requirements data.")
    public ResponseEntity<List<String>> getAvailableSources() {
        return ResponseEntity.ok(requirementAnalyticsFacade.getAvailableSources());
    }

    private List<String> parseSources(String sources) {
        if (sources == null || sources.isBlank()) return Collections.emptyList();
        return Arrays.asList(sources.split(","));
    }
}
