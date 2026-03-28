package com.passerellerh.service;

import com.passerellerh.dto.AdminStatsDTO;
import com.passerellerh.dto.UserResponseDTO;
import com.passerellerh.enums.Role;
import com.passerellerh.enums.StatutMission;
import com.passerellerh.repository.CompetenceRepository;
import com.passerellerh.repository.MissionRepository;
import com.passerellerh.repository.UserRepository;
import com.passerellerh.repository.ValidationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final ValidationRepository validationRepository;
    private final CompetenceRepository competenceRepository;
    private final com.passerellerh.repository.BadgeRepository badgeRepository;
    private final com.passerellerh.mapper.UserMapper userMapper;

    public AdminStatsDTO getGlobalStats() {
        Map<String, Long> usersByRole = new HashMap<>();
        for (Role role : Role.values()) {
            usersByRole.put(role.name(), userRepository.countByRole(role));
        }

        Map<String, Long> missionsByStatus = new HashMap<>();
        for (StatutMission statut : StatutMission.values()) {
            missionsByStatus.put(statut.name(), missionRepository.countByStatut(statut));
        }

        return AdminStatsDTO.builder()
                .totalUsers(userRepository.count())
                .totalMissions(missionRepository.count())
                .totalValidations(validationRepository.count())
                .usersByRole(usersByRole)
                .missionsByStatus(missionsByStatus)
                .topCompetences(getTopCompetences())
                .build();
    }

    private Map<String, Long> getTopCompetences() {
        // Basic implementation: count how many times each competence is Mobilized
        return missionRepository.findAll().stream()
                .flatMap(m -> m.getCompetencesMobilisees().stream())
                .collect(Collectors.groupingBy(c -> c.getNom(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // --- User Management ---

    public java.util.List<com.passerellerh.dto.UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les utilisateurs de façon paginée avec recherche et filtrage par rôle.
     */
    public Page<UserResponseDTO> getUsersPaginated(String search, Role role, Pageable pageable) {
        if (search != null && !search.isBlank() && role != null) {
            return userRepository.findBySearchAndRole(search.toLowerCase(), role, pageable)
                    .map(userMapper::toDto);
        } else if (search != null && !search.isBlank()) {
            return userRepository.findBySearch(search.toLowerCase(), pageable)
                    .map(userMapper::toDto);
        } else if (role != null) {
            return userRepository.findByRole(role, pageable)
                    .map(userMapper::toDto);
        }
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    public void updateUserRole(Long userId, Role newRole) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setRole(newRole);
            userRepository.save(user);
        });
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // --- Competence CRUD ---

    public java.util.List<com.passerellerh.dto.CompetenceDTO> getAllCompetences() {
        return competenceRepository.findAll().stream()
                .map(userMapper::toCompetenceDto)
                .collect(Collectors.toList());
    }

    public com.passerellerh.dto.CompetenceDTO saveCompetence(com.passerellerh.dto.CompetenceDTO dto) {
        com.passerellerh.entity.Competence competence = userMapper.toEntity(dto);
        return userMapper.toCompetenceDto(competenceRepository.save(competence));
    }

    public void deleteCompetence(Long id) {
        competenceRepository.deleteById(id);
    }

    // --- Badge CRUD ---

    public java.util.List<com.passerellerh.dto.BadgeDTO> getAllBadges() {
        return badgeRepository.findAll().stream()
                .map(userMapper::toBadgeDto)
                .collect(Collectors.toList());
    }

    public com.passerellerh.dto.BadgeDTO saveBadge(com.passerellerh.dto.BadgeDTO dto) {
        com.passerellerh.entity.Badge badge = userMapper.toEntity(dto);
        return userMapper.toBadgeDto(badgeRepository.save(badge));
    }

    public void deleteBadge(Long id) {
        badgeRepository.deleteById(id);
    }
    public List<User> getAdmins(){
        return
                userRepository.findAll().stream()
                        .filter(user::getRole==Role.ADMIN)
                        user->user.getRole()
                        .tolist();
    }
}



