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
                    com.passerellerh.dto.PublicProfileDTO dto = new com.passerellerh.dto.PublicProfileDTO();
                    dto.setNom(user.getNom());
                    dto.setPrenom(user.getPrenom());
                    dto.setScoreFiabilite(user.getScoreFiabilite());

                    com.passerellerh.dto.UserResponseDTO userDto = userMapper.toDto(user);
                    dto.setCompetences(userDto.getCompetences());
                    dto.setBadges(userDto.getBadges());

                    java.util.List<com.passerellerh.dto.PublicProfileDTO.PublicMissionDTO> publicMissions = missionRepository
                            .findByUtilisateurIdAndStatut(user.getId(), com.passerellerh.enums.StatutMission.VALIDATED)
                            .stream()
                            .map(m -> {
                                com.passerellerh.dto.PublicProfileDTO.PublicMissionDTO pm = new com.passerellerh.dto.PublicProfileDTO.PublicMissionDTO();
                                pm.setTitre(m.getTitre());
                                if (m.getValidation() != null) {
                                    pm.setDateValid(m.getValidation().getDateValidation().toString());
                                    pm.setValidateurName(m.getValidation().getValidateur().getPrenom() + " "
                                            + m.getValidation().getValidateur().getNom());
                                }
                                return pm;
                            })
                            .collect(java.util.stream.Collectors.toList());

                    dto.setMissions(publicMissions);
                    return ResponseEntity.ok(dto);
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
