package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationNoteRepository extends MongoRepository<ApplicationNote, String> {


    List<ApplicationNote> findByCompanyName(String companyName);
    List<ApplicationNote> findByCompanyNameIgnoreCase(String companyName);
}
