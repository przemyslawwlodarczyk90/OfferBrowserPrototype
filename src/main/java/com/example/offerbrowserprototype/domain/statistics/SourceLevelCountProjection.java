package com.example.offerbrowserprototype.domain.statistics;

public interface SourceLevelCountProjection {
    String getSource();
    String getLevel();
    Long getCount();
}
