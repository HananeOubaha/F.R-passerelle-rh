package com.passerellerh.repository;

import com.passerellerh.entity.User;
import com.passerellerh.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByProfilePublicToken(String profilePublicToken);

    boolean existsByEmail(String email);

    java.util.List<User> findByRole(Role role);

    long countByRole(Role role);

    // Méthodes paginées
    Page<User> findByRole(Role role, Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.nom) LIKE %:search% OR LOWER(u.prenom) LIKE %:search% OR LOWER(u.email) LIKE %:search%")
    Page<User> findBySearch(@Param("search") String search, Pageable pageable);

    @Query("SELECT u FROM User u WHERE (LOWER(u.nom) LIKE %:search% OR LOWER(u.prenom) LIKE %:search% OR LOWER(u.email) LIKE %:search%) AND u.role = :role")
    Page<User> findBySearchAndRole(@Param("search") String search, @Param("role") Role role, Pageable pageable);
}
