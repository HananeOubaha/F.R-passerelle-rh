package com.passerellerh.controller;

import com.passerellerh.dto.CreateMissionRequest;
import com.passerellerh.dto.MissionDTO;
import com.passerellerh.entity.Mission;
import com.passerellerh.entity.Utilisateur;
import com.passerellerh.enums.StatutMission;
import com.passerellerh.repository.MissionRepository;
import com.passerellerh.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class MissionController {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final com.passerellerh.repository.CompetenceRepository competenceRepository;
    private final com.passerellerh.service.EmailService emailService;

    @GetMapping
    public ResponseEntity<List<MissionDTO>> getMissions(@AuthenticationPrincipal UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .filter(Utilisateur.class::isInstance)
                .map(Utilisateur.class::cast)
                .map(u -> {
                    List<MissionDTO> missions = missionRepository.findByUtilisateurIdWithCompetences(u.getId())
                            .stream().map(this::toDto).collect(Collectors.toList());
                    return ResponseEntity.ok(missions);
                })
                .orElse(ResponseEntity.ok(List.of()));
    }

    @PostMapping
    public ResponseEntity<MissionDTO> createMission(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateMissionRequest request) {

        return userRepository.findByEmail(userDetails.getUsername())
                .filter(Utilisateur.class::isInstance)
                .map(Utilisateur.class::cast)
                .map(utilisateur -> {
                    Mission mission = new Mission();
                    mission.setTitre(request.getTitre());
                    mission.setDescription(request.getDescription());
                    mission.setDateDebut(request.getDateDebut());
                    mission.setDateFin(request.getDateFin());
                    mission.setValidatorEmail(request.getValidatorEmail());
                    mission.setStatut(StatutMission.PENDING);
                    mission.setValidationToken(UUID.randomUUID().toString());
                    mission.setUtilisateur(utilisateur);

                    if (request.getCompetenceIds() != null && !request.getCompetenceIds().isEmpty()) {
                        mission.setCompetencesMobilisees(new java.util.HashSet<>(
                                competenceRepository.findAllById(request.getCompetenceIds())));
                    }

                    Mission saved = missionRepository.save(mission);

                    // Envoyer une invitation par email au validateur
                    emailService.sendValidationInvitation(saved);

                    return ResponseEntity.ok(toDto(saved));
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        return missionRepository.findById(id)
                .filter(m -> m.getUtilisateur().getEmail().equals(userDetails.getUsername()))
                .filter(m -> m.getStatut() == StatutMission.PENDING)
                .map(m -> {
                    missionRepository.delete(m);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private MissionDTO toDto(Mission mission) {
        MissionDTO dto = new MissionDTO();
        dto.setId(mission.getId());
        dto.setTitre(mission.getTitre());
        dto.setDescription(mission.getDescription());
        dto.setDateDebut(mission.getDateDebut());
        dto.setDateFin(mission.getDateFin());
        dto.setStatut(mission.getStatut());
        dto.setValidatorEmail(mission.getValidatorEmail());

        if (mission.getCompetencesMobilisees() != null) {
            dto.setCompetencesMobilisees(mission.getCompetencesMobilisees().stream()
                    .map(c -> {
                        com.passerellerh.dto.CompetenceDTO cDto = new com.passerellerh.dto.CompetenceDTO();
                        cDto.setId(c.getId());
                        cDto.setNom(c.getNom());
                        return cDto;
                    }).collect(java.util.stream.Collectors.toList()));
        }

        return dto;
    }
}
