package com.passerellerh.dto;

import com.passerellerh.enums.StatutMission;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MissionDTO {
    private Long id;
    private String titre;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutMission statut;
    private String validatorEmail;
    private java.util.List<CompetenceDTO> competencesMobilisees;
}
