package com.passerellerh.service;

import com.passerellerh.entity.Badge;
import com.passerellerh.entity.PasseportRH;
import com.passerellerh.entity.Utilisateur;
import com.passerellerh.repository.BadgeRepository;
import com.passerellerh.repository.PasseportRHRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasseportService {

    private final PasseportRHRepository passeportRHRepository;
    private final BadgeRepository badgeRepository;

    @Transactional
    public PasseportRH checkAndAssignBadges(Utilisateur utilisateur) {
        PasseportRH passeport = passeportRHRepository.findByUtilisateurId(utilisateur.getId())
                .orElse(null);

        if (passeport == null) {
            passeport = new PasseportRH();
            passeport.setUtilisateur(utilisateur);
            passeport = passeportRHRepository.save(passeport);
        }

        double userScore = utilisateur.getScoreFiabilite() != null ? utilisateur.getScoreFiabilite() : 0.0;
        List<Badge> allBadges = badgeRepository.findAll();
        List<Badge> earnedBadges = new ArrayList<>();

        for (Badge badge : allBadges) {
            // Un utilisateur gagne le badge si son score est supérieur ou égal au seuil du
            // badge
            if (userScore >= badge.getSeuilScore()) {
                earnedBadges.add(badge);
            }
        }

        passeport.setBadges(earnedBadges);
        return passeportRHRepository.save(passeport);
    }
}
