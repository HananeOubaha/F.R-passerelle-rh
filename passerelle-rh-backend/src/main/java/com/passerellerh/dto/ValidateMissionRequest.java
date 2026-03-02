package com.passerellerh.dto;

import lombok.Data;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Data
public class ValidateMissionRequest {
    private java.util.Map<Long, @jakarta.validation.constraints.Min(1) @jakarta.validation.constraints.Max(5) Integer> competenceScores;
    private String commentaire;
}
