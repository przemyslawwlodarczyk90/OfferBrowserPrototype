package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationNoteRepository extends JpaRepository<ApplicationNote, Long> {

    long countByUserId(String userId);

    List<ApplicationNote> findByUserId(String userId);

    List<ApplicationNote> findByUserIdAndCompanyNameIgnoreCase(String userId, String companyName);
}
