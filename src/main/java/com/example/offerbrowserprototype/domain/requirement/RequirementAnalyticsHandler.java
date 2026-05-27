package com.example.offerbrowserprototype.domain.requirement;

import com.example.offerbrowserprototype.domain.dto.analytics.SkillCountDTO;
import com.example.offerbrowserprototype.infrastructure.repository.RequirementRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequirementAnalyticsHandler {

    private final RequirementRepository requirementRepository;

    public RequirementAnalyticsHandler(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    public List<SkillCountDTO> getTopSkills(List<String> sources) {
        List<SkillCountProjection> raw = (sources == null || sources.isEmpty())
                ? requirementRepository.findTopSkills(Pageable.unpaged())
                : requirementRepository.findTopSkillsBySources(sources, Pageable.unpaged());
        return raw.stream().map(p -> new SkillCountDTO(p.getSkill(), p.getCount())).collect(Collectors.toList());
    }

    public List<SkillCountDTO> getTopSkillsByLevel(String level, List<String> sources) {
        List<SkillCountProjection> raw = (sources == null || sources.isEmpty())
                ? requirementRepository.findTopSkillsByLevel(level, Pageable.unpaged())
                : requirementRepository.findTopSkillsByLevelAndSources(level, sources, Pageable.unpaged());
        return raw.stream().map(p -> new SkillCountDTO(p.getSkill(), p.getCount())).collect(Collectors.toList());
    }

    public List<String> getAvailableLevels() {
        return requirementRepository.findDistinctLevels();
    }

    public List<String> getAvailableSources() {
        return requirementRepository.findDistinctSources();
    }
}
