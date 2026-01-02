package com.passerellerh.repository;

import com.passerellerh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    java.util.List<User> findByRole(com.passerellerh.enums.Role role);

    long countByRole(com.passerellerh.enums.Role role);

    java.util.Optional<User> findByProfilePublicToken(String token);
}
