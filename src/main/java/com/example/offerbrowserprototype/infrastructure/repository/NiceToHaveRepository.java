package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.requirement.NiceToHave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NiceToHaveRepository extends JpaRepository<NiceToHave, Long> {

    /** Nice-to-have facts created before offers carried a source all come from No Fluff Jobs. */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE NiceToHave n SET n.source = :source WHERE n.source IS NULL")
    int backfillSource(@Param("source") String source);
}
