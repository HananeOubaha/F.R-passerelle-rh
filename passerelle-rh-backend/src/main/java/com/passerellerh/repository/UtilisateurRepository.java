package com.passerellerh.repository;

import com.passerellerh.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);

    Optional<Utilisateur> findByProfilePublicToken(String token);

    @Query("SELECT u FROM Utilisateur u WHERE u.ville = :ville")
    List<Utilisateur> findByVille(String ville);

    @Query("SELECT COUNT(u) FROM Utilisateur u")
    long countAllUtilisateurs();
}
