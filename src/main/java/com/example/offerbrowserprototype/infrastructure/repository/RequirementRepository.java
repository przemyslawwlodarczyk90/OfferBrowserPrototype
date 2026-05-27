package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.requirement.Requirement;
import com.example.offerbrowserprototype.domain.requirement.SkillCountProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    @Query("SELECT r.skill AS skill, COUNT(r) AS count FROM Requirement r GROUP BY r.skill ORDER BY COUNT(r) DESC")
    List<SkillCountProjection> findTopSkills(Pageable pageable);

    @Query("SELECT r.skill AS skill, COUNT(r) AS count FROM Requirement r WHERE r.level = :level GROUP BY r.skill ORDER BY COUNT(r) DESC")
    List<SkillCountProjection> findTopSkillsByLevel(@Param("level") String level, Pageable pageable);

    @Query("SELECT DISTINCT r.level FROM Requirement r WHERE r.level IS NOT NULL ORDER BY r.level")
    List<String> findDistinctLevels();
}

