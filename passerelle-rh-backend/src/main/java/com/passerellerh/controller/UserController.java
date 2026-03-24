package com.passerellerh.controller;

import com.passerellerh.entity.User;
import com.passerellerh.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final com.passerellerh.mapper.UserMapper userMapper;
    private final com.passerellerh.repository.CompetenceRepository competenceRepository;
    private final com.passerellerh.repository.MissionRepository missionRepository;

    @GetMapping("/public/{token}")
    public ResponseEntity<com.passerellerh.dto.PublicProfileDTO> getPublicProfile(
            @org.springframework.web.bind.annotation.PathVariable String token) {

        return userRepository.findByProfilePublicToken(token)
                .map(user -> {
                    // Mapping de base
                    com.passerellerh.dto.PublicProfileDTO.PublicProfileDTOBuilder builder =
                            com.passerellerh.dto.PublicProfileDTO.builder()
                            .nom(user.getNom())
                            .prenom(user.getPrenom())
                            .ville(user.getVille())
                            .scoreFiabilite(user.getScoreFiabilite());

                    // Mapping spécifique Utilisateur (Badges, Compétences, Missions)
                    if (user instanceof com.passerellerh.entity.Utilisateur) {
                        com.passerellerh.entity.Utilisateur utilisateur = (com.passerellerh.entity.Utilisateur) user;

                        // Compétences
                        if (utilisateur.getCompetences() != null) {
                            java.util.List<String> competences = utilisateur.getCompetences().stream()
                                    .map(com.passerellerh.entity.Competence::getNom)
                                    .collect(java.util.stream.Collectors.toList());
                            builder.competences(competences);
                        }

                        // Badges (via Passeport)
                        if (utilisateur.getPasseport() != null && utilisateur.getPasseport().getBadges() != null) {
                            java.util.List<com.passerellerh.dto.PublicProfileDTO.BadgeDTO> badges =
                                    utilisateur.getPasseport().getBadges().stream()
                                    .map(b -> com.passerellerh.dto.PublicProfileDTO.BadgeDTO.builder()
                                            .nom(b.getNom())
                                            .niveau(b.getNiveau() != null ? b.getNiveau().name() : null)
                                            .build())
                                    .collect(java.util.stream.Collectors.toList());
                            builder.badges(badges);
                        }

                        // Missions Validées
                        java.util.List<com.passerellerh.dto.PublicProfileDTO.PublicMissionDTO> missions =
                                missionRepository.findByUtilisateurIdAndStatut(
                                        utilisateur.getId(), com.passerellerh.enums.StatutMission.VALIDATED)
                                .stream()
                                .map(m -> com.passerellerh.dto.PublicProfileDTO.PublicMissionDTO.builder()
                                        .titre(m.getTitre())
                                        .description(m.getDescription())
                                        .dateDebut(m.getDateDebut())
                                        .dateFin(m.getDateFin())
                                        .validateurName(m.getValidation() != null && m.getValidation().getValidateur() != null ?
                                                m.getValidation().getValidateur().getPrenom() + " " + m.getValidation().getValidateur().getNom() :
                                                "Validateur inconnu")
                                        .evaluation(m.getValidation() != null ? m.getValidation().getCommentaire() : null)
                                        .build())
                                .collect(java.util.stream.Collectors.toList());
                        builder.missions(missions);
                    }

                    return ResponseEntity.ok(builder.build());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<com.passerellerh.dto.UserResponseDTO> getMe(
            @AuthenticationPrincipal UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @org.springframework.web.bind.annotation.PutMapping("/me")
    public ResponseEntity<com.passerellerh.dto.UserResponseDTO> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @org.springframework.web.bind.annotation.RequestBody com.passerellerh.dto.UpdateProfileRequest request) {

        return userRepository.findByEmail(userDetails.getUsername())
                .map(user -> {
                    userMapper.updateUserFromDto(request, user);
                    User savedUser = userRepository.save(user);
                    return ResponseEntity.ok(userMapper.toDto(savedUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @org.springframework.web.bind.annotation.PostMapping("/me/competences")
    public ResponseEntity<com.passerellerh.dto.UserResponseDTO> addCompetence(
            @AuthenticationPrincipal UserDetails userDetails,
            @org.springframework.web.bind.annotation.RequestBody com.passerellerh.dto.CompetenceDTO competenceDto) {

        return userRepository.findByEmail(userDetails.getUsername())
                .filter(com.passerellerh.entity.Utilisateur.class::isInstance)
                .map(com.passerellerh.entity.Utilisateur.class::cast)
                .map(utilisateur -> {
                    com.passerellerh.entity.Competence competence = competenceRepository
                            .findByNom(competenceDto.getNom())
                            .orElseGet(() -> {
                                com.passerellerh.entity.Competence newComp = new com.passerellerh.entity.Competence();
                                newComp.setNom(competenceDto.getNom());
                                return competenceRepository.save(newComp);
                            });

                    if (utilisateur.getCompetences() == null) {
                        utilisateur.setCompetences(new java.util.HashSet<>());
                    }
                    utilisateur.getCompetences().add(competence);
                    userRepository.save(utilisateur);
                    return ResponseEntity.ok(userMapper.toDto(utilisateur));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/me/competences/{id}")
    public ResponseEntity<com.passerellerh.dto.UserResponseDTO> removeCompetence(
            @AuthenticationPrincipal UserDetails userDetails,
            @org.springframework.web.bind.annotation.PathVariable Long id) {

        return userRepository.findByEmail(userDetails.getUsername())
                .filter(com.passerellerh.entity.Utilisateur.class::isInstance)
                .map(com.passerellerh.entity.Utilisateur.class::cast)
                .map(utilisateur -> {
                    if (utilisateur.getCompetences() != null) {
                        utilisateur.getCompetences().removeIf(c -> c.getId().equals(id));
                        userRepository.save(utilisateur);
                    }
                    return ResponseEntity.ok(userMapper.toDto(utilisateur));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
