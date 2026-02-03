package com.passerellerh.dto;

import lombok.Data;
import java.util.List;

@Data
public class PublicProfileDTO {
    private String nom;
    private String prenom;
    private Double scoreFiabilite;
    private List<CompetenceDTO> competences;
    private List<BadgeDTO> badges;
    private List<PublicMissionDTO> missions;

    @Data
    public static class PublicMissionDTO {
        private String titre;
        private String dateValid;
        private String validateurName;
    }
}
