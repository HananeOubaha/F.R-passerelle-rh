package com.passerellerh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.passerellerh.enums.Niveau;

@Entity
@Table(name = "badges")
@Getter
@Setter
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    private int seuilMissions;
    private double seuilScore;
    private String iconeUrl;
    private String messageFelicitation;

    @Enumerated(EnumType.STRING)
    private Niveau niveau;
}
