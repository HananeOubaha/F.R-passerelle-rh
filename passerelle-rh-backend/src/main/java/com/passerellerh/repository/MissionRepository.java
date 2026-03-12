package com.passerellerh.repository;

import com.passerellerh.entity.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {
        @org.springframework.data.jpa.repository.Query("SELECT m FROM Mission m LEFT JOIN FETCH m.competencesMobilisees WHERE m.utilisateur.id = :userId ORDER BY m.dateDebut DESC")
        java.util.List<Mission> findByUtilisateurIdWithCompetences(
                        @org.springframework.data.repository.query.Param("userId") Long userId);

        @org.springframework.data.jpa.repository.Query("SELECT m FROM Mission m LEFT JOIN FETCH m.competencesMobilisees WHERE m.statut = 'PENDING' AND LOWER(m.validatorEmail) = LOWER(:email)")
        java.util.List<Mission> findPendingWithCompetences(
                        @org.springframework.data.repository.query.Param("email") String email);

        List<Mission> findByUtilisateurIdOrderByDateDebutDesc(Long utilisateurId);

        long countByStatut(com.passerellerh.enums.StatutMission statut);

        java.util.List<Mission> findByUtilisateurIdAndStatut(Long userId, com.passerellerh.enums.StatutMission statut);

        // Méthodes paginées
        Page<Mission> findByUtilisateurId(Long utilisateurId, Pageable pageable);

        Page<Mission> findByUtilisateurIdAndStatut(Long utilisateurId, com.passerellerh.enums.StatutMission statut, Pageable pageable);
}
