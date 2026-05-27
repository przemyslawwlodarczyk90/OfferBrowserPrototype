package com.example.offerbrowserprototype.domain.requirement;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.infrastructure.repository.RequirementRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequirementCreationHandler {

    private final RequirementRepository requirementRepository;

    public RequirementCreationHandler(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    public void createFromOffer(Offer offer) {
        List<String> skills = offer.getRequirements();
        if (skills == null || skills.isEmpty()) return;

        for (String skill : skills) {
            requirementRepository.save(
                    Requirement.builder()
                            .skill(skill)
                            .source(offer.getSource())
                            .level(offer.getLevel())
                            .build()
            );
        }
    }
}
