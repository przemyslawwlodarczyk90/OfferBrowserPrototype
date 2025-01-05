package com.example.offerbrowserprototype.domain.mapper;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CityDistributionMapper {

    public Map<String, Long> toMap(Map<String, Long> cityDistribution) {
        return cityDistribution.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
