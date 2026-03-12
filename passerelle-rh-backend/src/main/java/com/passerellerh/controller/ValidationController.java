package com.passerellerh.controller;

import com.passerellerh.dto.MissionDTO;
import com.passerellerh.dto.ValidateMissionRequest;
import com.passerellerh.dto.ValidationDTO;
import com.passerellerh.entity.Mission;
import com.passerellerh.entity.Utilisateur;
import com.passerellerh.entity.Validateur;
import com.passerellerh.entity.Validation;
import com.passerellerh.enums.StatutMission;
import com.passerellerh.repository.MissionRepository;
import com.passerellerh.repository.UserRepository;
import com.passerellerh.repository.ValidationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/validation")
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class ValidationController {

    private final MissionRepository missionRepository;
    private final ValidationRepository validationRepository;
    private final UserRepository userRepository;
    private final com.passerellerh.repository.CompetenceRepository competenceRepository;
    private final com.passerellerh.service.ValidationService validationService;
    private final com.passerellerh.service.EmailService emailService;

    /** List all PENDING missions visible to a Validateur */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('VALIDATOR')")
    public ResponseEntity<List<MissionDTO>> getPendingMissions(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<MissionDTO> missions = missionRepository.findPendingWithCompetences(email).stream()
                .map(this::toMissionDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(missions);
    }

    /** Validate a mission with scores */
    @PostMapping("/validate/{missionId}")
    @PreAuthorize("hasRole('VALIDATOR')")
    public ResponseEntity<ValidationDTO> validateMission(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long missionId,
            @Valid @RequestBody ValidateMissionRequest request) {

        try {
            Validation validation = validationService.validateMission(missionId, userDetails.getUsername(), request);
            return ResponseEntity.ok(toDto(validation));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /** Reject a mission */
    @PostMapping("/reject/{missionId}")
    @PreAuthorize("hasRole('VALIDATOR')")
    public ResponseEntity<MissionDTO> rejectMission(
            @PathVariable Long missionId,
            @RequestParam(required = false) String reason) {

        return missionRepository.findById(missionId)
                .filter(m -> m.getStatut() == StatutMission.PENDING)
                .map(m -> {
                    m.setStatut(StatutMission.REJECTED);
                    missionRepository.save(m);
                    // Notifier l'utilisateur par email du rejet
                    emailService.sendMissionRejectedNotification(m, reason);
                    return ResponseEntity.ok(toMissionDto(m));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('VALIDATOR')")
    public ResponseEntity<List<ValidationDTO>> getMyValidationHistory(
            @AuthenticationPrincipal UserDetails userDetails) {

        return userRepository.findByEmail(userDetails.getUsername())
                .filter(Validateur.class::isInstance)
                .map(Validateur.class::cast)
                .map(v -> {
                    List<ValidationDTO> history = validationRepository.findByValidateurId(v.getId())
                            .stream().map(this::toDto).collect(Collectors.toList());
                    return ResponseEntity.ok(history);
                })
                .orElse(ResponseEntity.ok(List.of()));
    }

    /** Get validations received by the logged-in User */
    @GetMapping("/my-validations")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ValidationDTO>> getReceivedValidations(
            @AuthenticationPrincipal UserDetails userDetails) {

        return userRepository.findByEmail(userDetails.getUsername())
                .filter(Utilisateur.class::isInstance)
                .map(Utilisateur.class::cast)
                .map(u -> {
                    List<ValidationDTO> history = validationRepository.findAll().stream()
                            .filter(v -> v.getMission().getUtilisateur().getId().equals(u.getId()))
                            .map(this::toDto)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(history);
                })
                .orElse(ResponseEntity.ok(List.of()));
    }

    private MissionDTO toMissionDto(Mission m) {
        MissionDTO dto = new MissionDTO();
        dto.setId(m.getId());
        dto.setTitre(m.getTitre());
        dto.setDescription(m.getDescription());
        dto.setDateDebut(m.getDateDebut());
        dto.setDateFin(m.getDateFin());
        dto.setStatut(m.getStatut());

        if (m.getCompetencesMobilisees() != null) {
            dto.setCompetencesMobilisees(m.getCompetencesMobilisees().stream()
                    .map(c -> {
                        com.passerellerh.dto.CompetenceDTO cDto = new com.passerellerh.dto.CompetenceDTO();
                        cDto.setId(c.getId());
                        cDto.setNom(c.getNom());
                        return cDto;
                    }).collect(Collectors.toList()));
        }

        return dto;
    }

    private ValidationDTO toDto(Validation v) {
        ValidationDTO dto = new ValidationDTO();
        dto.setId(v.getId());

        if (v.getScores() != null) {
            java.util.Map<String, Integer> scoreMap = new java.util.HashMap<>();
            for (com.passerellerh.entity.CompetenceScore s : v.getScores()) {
                scoreMap.put(s.getCompetence().getNom(), s.getScore());
            }
            dto.setCompetenceScores(scoreMap);
        }

        dto.setCommentaire(v.getCommentaire());
        dto.setDateValidation(v.getDateValidation());
        dto.setMissionTitre(v.getMission().getTitre());
        dto.setValidateurNom(v.getValidateur().getNom() + " " + v.getValidateur().getPrenom());
        return dto;
    }
}
