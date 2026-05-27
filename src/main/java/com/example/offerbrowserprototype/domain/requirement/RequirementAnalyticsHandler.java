package com.example.offerbrowserprototype.domain.requirement;

import com.example.offerbrowserprototype.domain.dto.analytics.SkillCountDTO;
import com.example.offerbrowserprototype.infrastructure.repository.RequirementRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequirementAnalyticsHandler {

    private final RequirementRepository requirementRepository;

    public RequirementAnalyticsHandler(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    public List<SkillCountDTO> getTopSkills(int limit) {
        return requirementRepository.findTopSkills(PageRequest.of(0, limit))
                .stream()
                .map(p -> new SkillCountDTO(p.getSkill(), p.getCount()))
                .collect(Collectors.toList());
    }

    public List<SkillCountDTO> getTopSkillsByLevel(String level, int limit) {
        return requirementRepository.findTopSkillsByLevel(level, PageRequest.of(0, limit))
                .stream()
                .map(p -> new SkillCountDTO(p.getSkill(), p.getCount()))
                .collect(Collectors.toList());
    }

    public List<String> getAvailableLevels() {
        return requirementRepository.findDistinctLevels();
    }
}
