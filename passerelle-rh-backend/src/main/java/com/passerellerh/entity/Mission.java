package com.passerellerh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import com.passerellerh.enums.StatutMission;

@Entity
@Table(name = "missions")
@Getter
@Setter
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutMission statut = StatutMission.PENDING;

    private String validatorEmail;

    private String validationToken;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL)
    private Validation validation;

    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL)
    private AttestationPDF attestation;

    @ManyToMany
    @JoinTable(name = "mission_competences", joinColumns = @JoinColumn(name = "mission_id"), inverseJoinColumns = @JoinColumn(name = "competence_id"))
    private java.util.Set<Competence> competencesMobilisees = new java.util.HashSet<>();
}
