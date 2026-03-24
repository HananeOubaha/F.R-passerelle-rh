package com.passerellerh.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PublicProfileDTO {
    private String nom;
    private String prenom;
    private String ville;
    private Double scoreFiabilite;
    private List<String> competences;
    private List<BadgeDTO> badges; // On suppose que BadgeDTO existe ou est interne
    private List<PublicMissionDTO> missions;

    @Data
    @Builder
    public static class PublicMissionDTO {
        private String titre;
        private String description;
        private LocalDate dateDebut;
        private LocalDate dateFin;
        private String validateurName;
        private String evaluation; // Ex: "Très bien"
    }

    @Data
    @Builder
    public static class BadgeDTO {
        private String nom;
        private String niveau;
    }
}
