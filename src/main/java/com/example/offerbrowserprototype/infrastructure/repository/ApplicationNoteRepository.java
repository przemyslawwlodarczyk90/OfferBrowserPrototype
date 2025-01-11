package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationNoteRepository extends MongoRepository<ApplicationNote, String> {


    List<ApplicationNote> findByCompanyNameIgnoreCase(String companyName);
    Optional<ApplicationNote> findByOfferUrl(String offerUrl);
    List<ApplicationNote> findByUserId(String userId);
    List<ApplicationNote> findByUserIdAndCompanyNameIgnoreCase(String userId, String companyName);
    long countByUserId(String userId);
}
