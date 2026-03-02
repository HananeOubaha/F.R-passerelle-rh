package com.passerellerh.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String nom;
    private String prenom;
    private String ville;
}
