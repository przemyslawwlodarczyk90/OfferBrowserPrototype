package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationNoteRepository extends JpaRepository<ApplicationNote, UUID> {

    long countByUserId(String userId);

    List<ApplicationNote> findByUserId(String userId);

    List<ApplicationNote> findByUserIdAndCompanyNameIgnoreCase(String userId, String companyName);
}
