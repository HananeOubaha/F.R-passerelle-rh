package com.passerellerh.service;

import com.passerellerh.entity.Badge;
import com.passerellerh.entity.PasseportRH;
import com.passerellerh.entity.Utilisateur;
import com.passerellerh.repository.BadgeRepository;
import com.passerellerh.repository.PasseportRHRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests - PasseportService (Attribution de Badges)")
class PasseportServiceTest {

    @Mock
    private PasseportRHRepository passeportRHRepository;
    @Mock
    private BadgeRepository badgeRepository;

    @InjectMocks
    private PasseportService passeportService;

    private Utilisateur utilisateur;
    private PasseportRH passeport;
    private Badge badgeDebutant;
    private Badge badgeConfirme;
    private Badge badgeFiable;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setEmail("user@test.com");

        passeport = new PasseportRH();
        passeport.setId(1L);
        passeport.setUtilisateur(utilisateur);

        badgeDebutant = new Badge();
        badgeDebutant.setId(1L);
        badgeDebutant.setNom("Débutant");
        badgeDebutant.setSeuilScore(0.0); // Obtenu dès le départ

        badgeConfirme = new Badge();
        badgeConfirme.setId(2L);
        badgeConfirme.setNom("Confirmé");
        badgeConfirme.setSeuilScore(50.0); // Score ≥ 50

        badgeFiable = new Badge();
        badgeFiable.setId(3L);
        badgeFiable.setNom("Fiable");
        badgeFiable.setSeuilScore(80.0); // Score ≥ 80
    }

    @Test
    @DisplayName("🏅 Score = 0 — seul le badge Débutant est attribué")
    void checkAndAssignBadges_score0_onlyDebutantBadge() {
        utilisateur.setScoreFiabilite(0.0);
        when(passeportRHRepository.findByUtilisateurId(1L)).thenReturn(Optional.of(passeport));
        when(badgeRepository.findAll()).thenReturn(List.of(badgeDebutant, badgeConfirme, badgeFiable));
        when(passeportRHRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PasseportRH result = passeportService.checkAndAssignBadges(utilisateur);

        assertEquals(1, result.getBadges().size());
        assertEquals("Débutant", result.getBadges().get(0).getNom());
    }

    @Test
    @DisplayName("🏅 Score = 60 — badges Débutant et Confirmé attribués")
    void checkAndAssignBadges_score60_debutantAndConfirme() {
        utilisateur.setScoreFiabilite(60.0);
        when(passeportRHRepository.findByUtilisateurId(1L)).thenReturn(Optional.of(passeport));
        when(badgeRepository.findAll()).thenReturn(List.of(badgeDebutant, badgeConfirme, badgeFiable));
        when(passeportRHRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PasseportRH result = passeportService.checkAndAssignBadges(utilisateur);

        assertEquals(2, result.getBadges().size());
        assertTrue(result.getBadges().stream().anyMatch(b -> b.getNom().equals("Débutant")));
        assertTrue(result.getBadges().stream().anyMatch(b -> b.getNom().equals("Confirmé")));
    }

    @Test
    @DisplayName("🏅 Score = 90 — tous les badges sont attribués")
    void checkAndAssignBadges_score90_allBadges() {
        utilisateur.setScoreFiabilite(90.0);
        when(passeportRHRepository.findByUtilisateurId(1L)).thenReturn(Optional.of(passeport));
        when(badgeRepository.findAll()).thenReturn(List.of(badgeDebutant, badgeConfirme, badgeFiable));
        when(passeportRHRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PasseportRH result = passeportService.checkAndAssignBadges(utilisateur);

        assertEquals(3, result.getBadges().size());
    }

    @Test
    @DisplayName("✅ Passeport inexistant — il est créé automatiquement")
    void checkAndAssignBadges_noPasseport_createsNewOne() {
        utilisateur.setScoreFiabilite(30.0);
        when(passeportRHRepository.findByUtilisateurId(1L)).thenReturn(Optional.empty());
        when(badgeRepository.findAll()).thenReturn(List.of(badgeDebutant));
        when(passeportRHRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PasseportRH result = passeportService.checkAndAssignBadges(utilisateur);

        assertNotNull(result);
        verify(passeportRHRepository, times(2)).save(any()); // Une fois pour créer, une fois pour mettre à jour les
                                                             // badges
    }
}
