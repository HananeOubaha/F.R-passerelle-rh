package com.passerellerh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "attestations_pdf")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttestationPDF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pdfUrl;
    private String qrCodeUrl;
    private LocalDate dateGeneration;

    @OneToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;
}