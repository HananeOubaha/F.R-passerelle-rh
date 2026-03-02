package com.passerellerh.dto;

import com.passerellerh.enums.Role;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String ville;
    private Role role;
    private Double scoreFiabilite;
    private java.util.List<CompetenceDTO> competences;
    private java.util.List<BadgeDTO> badges;
    private String profilePublicToken;
}
