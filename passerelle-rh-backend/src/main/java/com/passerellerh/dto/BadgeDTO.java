package com.passerellerh.dto;

import lombok.Data;

@Data
public class BadgeDTO {
    private Long id;
    private String nom;
    private double seuilScore;
    private String iconeUrl;
}
