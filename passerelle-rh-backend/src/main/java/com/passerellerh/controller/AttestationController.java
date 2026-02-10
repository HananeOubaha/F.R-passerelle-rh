package com.passerellerh.controller;

import com.passerellerh.entity.Mission;
import com.passerellerh.entity.Validation;
import com.passerellerh.enums.StatutMission;
import com.passerellerh.repository.MissionRepository;
import com.passerellerh.repository.ValidationRepository;
import com.passerellerh.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attestations")
@RequiredArgsConstructor
public class AttestationController {

    private final MissionRepository missionRepository;
    private final ValidationRepository validationRepository;
    private final PdfService pdfService;

    @GetMapping("/download/{missionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> downloadAttestation(@PathVariable Long missionId) {
        try {
            Mission mission = missionRepository.findById(missionId)
                    .orElseThrow(() -> new RuntimeException("Mission non trouvée"));

            if (mission.getStatut() != StatutMission.VALIDATED) {
                return ResponseEntity.badRequest().build();
            }

            Validation validation = validationRepository.findByMissionId(missionId)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Validation non trouvée pour cette mission"));

            // Le QR Code pointera vers l'URL du frontend qui vérifiera l'authenticité
            String qrCodeData = "http://localhost:4200/verify/" + missionId;

            byte[] pdfBytes = pdfService.generateAttestation(mission, validation, qrCodeData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "attestation_" + missionId + ".pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/verify/{missionId}")
    public ResponseEntity<?> verifyAttestation(@PathVariable Long missionId) {
        try {
            Mission mission = missionRepository.findById(missionId).orElse(null);

            if (mission == null || mission.getStatut() != StatutMission.VALIDATED) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Mission invalide ou introuvable.");
            }

            Validation validation = validationRepository.findByMissionId(missionId)
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (validation == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Validation introuvable.");
            }

            // Retourner des informations simples pour prouver l'authenticité
            return ResponseEntity.ok(java.util.Map.of(
                    "status", "AUTHENTIC",
                    "utilisateur", mission.getUtilisateur().getPrenom() + " " + mission.getUtilisateur().getNom(),
                    "mission", mission.getTitre(),
                    "dateValidation", validation.getDateValidation(),
                    "validateur", validation.getValidateur().getPrenom() + " " + validation.getValidateur().getNom()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
