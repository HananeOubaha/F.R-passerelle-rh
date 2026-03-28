package com.passerellerh.service;

import com.passerellerh.dto.ValidateMissionRequest;
import com.passerellerh.entity.*;
import com.passerellerh.enums.StatutMission;
import com.passerellerh.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final MissionRepository missionRepository;
    private final ValidationRepository validationRepository;
    private final UserRepository userRepository;
    private final CompetenceRepository competenceRepository;
    private final PasseportService passeportService;
    private final EmailService emailService;

    @Transactional
    public Validation validateMission(Long missionId, String validatorEmail, ValidateMissionRequest request) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée"));

        if (mission.getStatut() != StatutMission.PENDING) {
            throw new RuntimeException("La mission n'est pas en attente de validation");
        }

        //   Verify that the authenticated validator is the one assigned
        // to this mission
        if (mission.getValidatorEmail() == null || !mission.getValidatorEmail().equalsIgnoreCase(validatorEmail)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à valider cette mission");
        }

        User user = userRepository.findByEmail(validatorEmail)
                .orElseThrow(() -> new RuntimeException("Validateur non trouvé"));

        if (!(user instanceof Validateur)) {
            throw new RuntimeException("L'utilisateur n'est pas un validateur autorisé");
        }
        Validateur validateur = (Validateur) user;

        Validation validation = new Validation();
        validation.setMission(mission);
        validation.setValidateur(validateur);
        validation.setCommentaire(request.getCommentaire());
        validation.setDateValidation(LocalDateTime.now());

        List<CompetenceScore> scores = new ArrayList<>();
        double totalScore = 0;
        int count = 0;

        if (request.getCompetenceScores() != null) {
            for (Map.Entry<Long, Integer> entry : request.getCompetenceScores().entrySet()) {
                Competence comp = competenceRepository.findById(entry.getKey())
                        .orElseThrow(() -> new RuntimeException("Compétence non trouvée: " + entry.getKey()));

                CompetenceScore cs = new CompetenceScore();
                cs.setCompetence(comp);
                cs.setScore(entry.getValue());
                cs.setValidation(validation);
                scores.add(cs);

                totalScore += entry.getValue();
                count++;
            }
        }
        validation.setScores(scores);
        Validation savedValidation = validationRepository.save(validation);

        // Update mission
        mission.setStatut(StatutMission.VALIDATED);
        // Link validation to mission for bidirectional OneToOne
        mission.setValidation(savedValidation);
        missionRepository.save(mission);

        // Update user score
        Utilisateur utilisateur = mission.getUtilisateur();
        if (utilisateur != null && count > 0) {
            double avg = totalScore / count;
            double currentScore = utilisateur.getScoreFiabilite() != null ? utilisateur.getScoreFiabilite() : 0.0;

            // Calcul : (score actuel + nouvelle moyenne sur 100) / 2
            double newScore = Math.round(((currentScore + avg * 20) / 2) * 10.0) / 10.0;
            utilisateur.setScoreFiabilite(newScore);
            userRepository.save(utilisateur);

            // Check badges
            passeportService.checkAndAssignBadges(utilisateur);
        }

        // Notifier l'utilisateur par email
        emailService.sendMissionValidatedNotification(mission);

        return savedValidation;
    }
}
