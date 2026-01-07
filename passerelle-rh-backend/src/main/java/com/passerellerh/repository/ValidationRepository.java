package com.passerellerh.repository;

import com.passerellerh.entity.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ValidationRepository extends JpaRepository<Validation, Long> {
    Optional<Validation> findByMissionId(Long missionId);

    List<Validation> findByValidateurId(Long validateurId);
}
