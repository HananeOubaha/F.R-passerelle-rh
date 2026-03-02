package com.passerellerh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "validations")
@Getter
@Setter
public class Validation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "validation", cascade = CascadeType.ALL)
    private java.util.List<CompetenceScore> scores;

    private String commentaire;
    private LocalDateTime dateValidation;

    @OneToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @ManyToOne
    @JoinColumn(name = "validateur_id")
    private Validateur validateur;
}
