package com.passerellerh.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "validateurs")
@Getter
@Setter
public class Validateur extends User {
    @OneToMany(mappedBy = "validateur")
    private List<Validation> validationsEffectuees;
}
