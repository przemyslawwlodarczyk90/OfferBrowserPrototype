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

    public List<SkillCountDTO> getTopSkills(int limit) {
        return requirementAnalyticsHandler.getTopSkills(limit);
    }

    public List<SkillCountDTO> getTopSkillsByLevel(String level, int limit) {
        return requirementAnalyticsHandler.getTopSkillsByLevel(level, limit);
    }

    public List<String> getAvailableLevels() {
        return requirementAnalyticsHandler.getAvailableLevels();
    }
}
