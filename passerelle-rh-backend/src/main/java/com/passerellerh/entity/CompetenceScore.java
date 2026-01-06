package com.passerellerh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "competence_scores")
@Getter
@Setter
public class CompetenceScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score; // 1-5

    @ManyToOne
    @JoinColumn(name = "competence_id")
    private Competence competence;

    @ManyToOne
    @JoinColumn(name = "validation_id")
    private Validation validation;
}
