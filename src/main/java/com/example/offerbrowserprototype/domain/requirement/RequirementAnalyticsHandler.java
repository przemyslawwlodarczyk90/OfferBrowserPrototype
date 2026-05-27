package com.example.offerbrowserprototype.domain.requirement;

import com.example.offerbrowserprototype.domain.dto.analytics.SkillCountDTO;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.RequirementRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequirementAnalyticsHandler {

    static final String UNKNOWN_LEVEL = "Nieokreślony poziom";

    private final RequirementRepository requirementRepository;
    private final OfferRepository offerRepository;

    public RequirementAnalyticsHandler(RequirementRepository requirementRepository,
                                       OfferRepository offerRepository) {
        this.requirementRepository = requirementRepository;
        this.offerRepository = offerRepository;
    }

    public List<SkillCountDTO> getTopSkills(List<String> sources) {
        List<SkillCountProjection> raw = (sources == null || sources.isEmpty())
                ? requirementRepository.findTopSkills(Pageable.unpaged())
                : requirementRepository.findTopSkillsBySources(sources, Pageable.unpaged());
        return raw.stream().map(p -> new SkillCountDTO(p.getSkill(), p.getCount())).collect(Collectors.toList());
    }

    public List<SkillCountDTO> getTopSkillsByLevel(String level, List<String> sources) {
        List<SkillCountProjection> raw;
        if (UNKNOWN_LEVEL.equals(level)) {
            raw = (sources == null || sources.isEmpty())
                    ? requirementRepository.findTopSkillsByNullLevel(Pageable.unpaged())
                    : requirementRepository.findTopSkillsByNullLevelAndSources(sources, Pageable.unpaged());
        } else {
            raw = (sources == null || sources.isEmpty())
                    ? requirementRepository.findTopSkillsByLevel(level, Pageable.unpaged())
                    : requirementRepository.findTopSkillsByLevelAndSources(level, sources, Pageable.unpaged());
        }
        return raw.stream().map(p -> new SkillCountDTO(p.getSkill(), p.getCount())).collect(Collectors.toList());
    }

    public List<String> getAvailableLevels() {
        List<String> levels = new ArrayList<>(requirementRepository.findDistinctLevels());
        if (requirementRepository.existsNullLevel()) {
            levels.add(UNKNOWN_LEVEL);
        }
        return levels;
    }

    public List<String> getAvailableSources() {
        return offerRepository.findDistinctSources();
    }
}
