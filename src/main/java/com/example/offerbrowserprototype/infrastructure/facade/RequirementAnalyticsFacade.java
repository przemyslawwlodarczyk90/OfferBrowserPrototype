package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.dto.analytics.SkillCountDTO;
import com.example.offerbrowserprototype.domain.requirement.RequirementAnalyticsHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequirementAnalyticsFacade {

    private final RequirementAnalyticsHandler requirementAnalyticsHandler;

    public RequirementAnalyticsFacade(RequirementAnalyticsHandler requirementAnalyticsHandler) {
        this.requirementAnalyticsHandler = requirementAnalyticsHandler;
    }

    public List<SkillCountDTO> getTopSkills(List<String> sources) {
        return requirementAnalyticsHandler.getTopSkills(sources);
    }

    public List<SkillCountDTO> getTopSkillsByLevel(String level, List<String> sources) {
        return requirementAnalyticsHandler.getTopSkillsByLevel(level, sources);
    }

    public List<String> getAvailableLevels() {
        return requirementAnalyticsHandler.getAvailableLevels();
    }

    public List<String> getAvailableSources() {
        return requirementAnalyticsHandler.getAvailableSources();
    }
}
