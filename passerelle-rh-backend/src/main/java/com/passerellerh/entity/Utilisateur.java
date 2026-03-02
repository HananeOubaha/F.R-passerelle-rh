package com.passerellerh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "utilisateurs")
@Getter
@Setter
public class Utilisateur extends User {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passeport_id")
    private PasseportRH passeport;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Mission> missions;

    @ManyToMany
    @JoinTable(name = "user_competences", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "competence_id"))
    private Set<Competence> competences;
}
