package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.requirement.NiceToHave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NiceToHaveRepository extends JpaRepository<NiceToHave, Long> {
}
