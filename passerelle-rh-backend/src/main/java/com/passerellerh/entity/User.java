package com.passerellerh.entity;

import com.passerellerh.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String ville;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Double scoreFiabilite = 0.0;

    private String profilePublicToken;

    @PrePersist
    public void onPrePersist() {
        if (this.profilePublicToken == null) {
            this.profilePublicToken = java.util.UUID.randomUUID().toString();
        }
    }
}