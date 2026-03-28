package com.passerellerh.service;

import com.passerellerh.dto.ValidateMissionRequest;
import com.passerellerh.entity.*;
import com.passerellerh.enums.StatutMission;
import com.passerellerh.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests - ValidationService")
class ValidationServiceTest {

    @Mock
    private MissionRepository missionRepository;
    @Mock
    private ValidationRepository validationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CompetenceRepository competenceRepository;
    @Mock
    private PasseportService passeportService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private ValidationService validationService;

    private Mission mission;
    private Validateur validateur;
    private Utilisateur utilisateur;
    private Competence competence;

    @BeforeEach
    void setUp() {
        competence = new Competence();
        competence.setId(1L);
        competence.setNom("Communication");

        utilisateur = new Utilisateur();
        utilisateur.setId(10L);
        utilisateur.setEmail("user@test.com");
        utilisateur.setScoreFiabilite(0.0);

        validateur = new Validateur();
        validateur.setId(20L);
        validateur.setEmail("validateur@test.com");

        mission = new Mission();
        mission.setId(1L);
        mission.setTitre("Mission Test");
        mission.setStatut(StatutMission.PENDING);
        mission.setValidatorEmail("validateur@test.com");
        mission.setUtilisateur(utilisateur);
        mission.setDateDebut(LocalDate.now().minusMonths(1));
        mission.setDateFin(LocalDate.now());
    }

    @Test
    @DisplayName(" Validation réussie — met à jour le statut et le score")
    void validateMission_success_updatesStatusAndScore() {
        // GIVEN
        ValidateMissionRequest request = new ValidateMissionRequest();
        request.setCommentaire("Excellent travail");
        request.setCompetenceScores(Map.of(1L, 4));

        when(missionRepository.findById(1L)).thenReturn(Optional.of(mission));
        when(userRepository.findByEmail("validateur@test.com")).thenReturn(Optional.of(validateur));
        when(competenceRepository.findById(1L)).thenReturn(Optional.of(competence));

        Validation savedValidation = new Validation();
        savedValidation.setId(100L);
        savedValidation.setMission(mission);
        savedValidation.setValidateur(validateur);
        when(validationRepository.save(any(Validation.class))).thenReturn(savedValidation);
        when(missionRepository.save(any(Mission.class))).thenReturn(mission);
        when(userRepository.save(any(User.class))).thenReturn(utilisateur);

        // WHEN
        Validation result = validationService.validateMission(1L, "validateur@test.com", request);

        // THEN
        assertNotNull(result);
        assertEquals(StatutMission.VALIDATED, mission.getStatut());
        // Score = (0 + 4*20) / 2 = 40.0
        assertEquals(40.0, utilisateur.getScoreFiabilite());
        verify(passeportService).checkAndAssignBadges(utilisateur);
    }

    @Test
    @DisplayName(" Mission introuvable — lève une exception")
    void validateMission_missionNotFound_throwsException() {
        when(missionRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> validationService.validateMission(999L, "validateur@test.com", new ValidateMissionRequest()));
        assertEquals("Mission non trouvée", ex.getMessage());
    }

    @Test
    @DisplayName(" Mission déjà validée — lève une exception")
    void validateMission_alreadyValidated_throwsException() {
        mission.setStatut(StatutMission.VALIDATED);
        when(missionRepository.findById(1L)).thenReturn(Optional.of(mission));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> validationService.validateMission(1L, "validateur@test.com", new ValidateMissionRequest()));
        assertEquals("La mission n'est pas en attente de validation", ex.getMessage());
    }

    @Test
    @DisplayName(" Sécurité: email du validateur ne correspond pas — lève une exception")
    void validateMission_wrongValidatorEmail_throwsSecurityException() {
        when(missionRepository.findById(1L)).thenReturn(Optional.of(mission));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> validationService.validateMission(1L, "hacker@evil.com", new ValidateMissionRequest()));
        assertEquals("Vous n'êtes pas autorisé à valider cette mission", ex.getMessage());
    }

    @Test
    @DisplayName(" Calcul du score — moyenne correcte sur plusieurs compétences")
    void validateMission_scoreCalculation_isCorrect() {
        // GIVEN : 3 compétences notées 4, 4, 4 → moyenne = 4 → score = (0 + 4*20) / 2 =
        // 40.0
        Competence c1 = new Competence();
        c1.setId(1L);
        c1.setNom("C1");
        Competence c2 = new Competence();
        c2.setId(2L);
        c2.setNom("C2");
        Competence c3 = new Competence();
        c3.setId(3L);
        c3.setNom("C3");

        ValidateMissionRequest request = new ValidateMissionRequest();
        request.setCompetenceScores(Map.of(1L, 4, 2L, 4, 3L, 4));

        when(missionRepository.findById(1L)).thenReturn(Optional.of(mission));
        when(userRepository.findByEmail("validateur@test.com")).thenReturn(Optional.of(validateur));
        when(competenceRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(competenceRepository.findById(2L)).thenReturn(Optional.of(c2));
        when(competenceRepository.findById(3L)).thenReturn(Optional.of(c3));
        when(validationRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(missionRepository.save(any())).thenReturn(mission);
        when(userRepository.save(any())).thenReturn(utilisateur);

        // WHEN
        validationService.validateMission(1L, "validateur@test.com", request);

        // THEN: (0 + (12/3 * 20)) / 2 = (0 + 80) / 2 = 40.0
        assertEquals(40.0, utilisateur.getScoreFiabilite());
    }

    @Test
    @DisplayName(" Calcul du score — amélioration progressive avec 2 validations")
    void validateMission_scoreImproves_withSecondValidation() {
        // Simule un utilisateur qui a déjà un score de 40 après une première validation
        utilisateur.setScoreFiabilite(40.0);
        // Deuxième validation avec note de 5 → moyenne = 5 → (40 + 5*20) / 2 = (40 +
        // 100) / 2 = 70.0
        ValidateMissionRequest request = new ValidateMissionRequest();
        request.setCompetenceScores(Map.of(1L, 5));

        when(missionRepository.findById(1L)).thenReturn(Optional.of(mission));
        when(userRepository.findByEmail("validateur@test.com")).thenReturn(Optional.of(validateur));
        when(competenceRepository.findById(1L)).thenReturn(Optional.of(competence));
        when(validationRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(missionRepository.save(any())).thenReturn(mission);
        when(userRepository.save(any())).thenReturn(utilisateur);

        validationService.validateMission(1L, "validateur@test.com", request);

        assertEquals(70.0, utilisateur.getScoreFiabilite());
    }
}
