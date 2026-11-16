package com.example.offerbrowserprototype.domain.dto.statistics;

import java.util.Map;

/** One experience level summed over all portals, with each portal's count. */
public record LevelBySourceDTO(String level, long total, Map<String, Long> bySource) {
}
