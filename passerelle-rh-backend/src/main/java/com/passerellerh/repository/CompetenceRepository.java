package com.passerellerh.repository;

import com.passerellerh.entity.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompetenceRepository extends JpaRepository<Competence, Long> {
    Optional<Competence> findByNom(String nom);
}
