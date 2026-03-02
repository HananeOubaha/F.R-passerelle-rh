package com.passerellerh.repository;

import com.passerellerh.entity.PasseportRH;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasseportRHRepository extends JpaRepository<PasseportRH, Long> {
    Optional<PasseportRH> findByUtilisateurId(Long utilisateurId);
}
