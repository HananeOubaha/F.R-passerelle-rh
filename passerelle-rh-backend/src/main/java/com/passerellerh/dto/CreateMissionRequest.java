package com.passerellerh.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateMissionRequest {
    @NotBlank
    private String titre;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Email
    private String validatorEmail;

    private java.util.List<Long> competenceIds;
}
