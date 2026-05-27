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
    @Operation(summary = "Top skills overall", description = "Returns the most frequently required skills across all offers.")
    public ResponseEntity<List<SkillCountDTO>> getTopSkills(
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(requirementAnalyticsFacade.getTopSkills(limit));
    }

    @GetMapping("/top-by-level")
    @Operation(summary = "Top skills by seniority level", description = "Returns the most frequently required skills for a given level.")
    public ResponseEntity<List<SkillCountDTO>> getTopSkillsByLevel(
            @RequestParam String level,
            @RequestParam(defaultValue = "15") int limit
    ) {
        return ResponseEntity.ok(requirementAnalyticsFacade.getTopSkillsByLevel(level, limit));
    }

    @GetMapping("/levels")
    @Operation(summary = "Available levels", description = "Returns distinct seniority levels present in requirements data.")
    public ResponseEntity<List<String>> getAvailableLevels() {
        return ResponseEntity.ok(requirementAnalyticsFacade.getAvailableLevels());
    }
}
