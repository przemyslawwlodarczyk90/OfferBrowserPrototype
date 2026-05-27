package com.example.offerbrowserprototype.domain.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SkillCountDTO {
    private String skill;
    private long count;
}
