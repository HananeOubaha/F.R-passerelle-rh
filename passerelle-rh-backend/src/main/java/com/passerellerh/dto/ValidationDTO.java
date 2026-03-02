package com.passerellerh.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ValidationDTO {
    private Long id;
    private java.util.Map<String, Integer> competenceScores;
    private String commentaire;
    private LocalDateTime dateValidation;
    private String missionTitre;
    private String validateurNom;
}
