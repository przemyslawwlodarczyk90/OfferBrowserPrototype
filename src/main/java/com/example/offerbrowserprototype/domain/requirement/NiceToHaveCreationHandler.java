package com.example.offerbrowserprototype.domain.requirement;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.infrastructure.repository.NiceToHaveRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NiceToHaveCreationHandler {

    private final NiceToHaveRepository niceToHaveRepository;

    public NiceToHaveCreationHandler(NiceToHaveRepository niceToHaveRepository) {
        this.niceToHaveRepository = niceToHaveRepository;
    }

    public void createFromOffer(Offer offer) {
        List<String> skills = offer.getNiceToHave();
        if (skills == null || skills.isEmpty()) return;

        for (String skill : skills) {
            niceToHaveRepository.save(
                    NiceToHave.builder()
                            .skill(skill)
                            .source(offer.getSource())
                            .level(offer.getLevel())
                            .build()
            );
        }
    }
}
