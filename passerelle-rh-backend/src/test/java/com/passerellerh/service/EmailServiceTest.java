package com.passerellerh.service;

import com.passerellerh.entity.Mission;
import com.passerellerh.entity.Utilisateur;
import com.passerellerh.enums.StatutMission;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests - EmailService")
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    private Mission mission;
    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "frontendUrl", "http://localhost:4200");
        ReflectionTestUtils.setField(emailService, "fromEmail", "test@passerelle-rh.com");

        utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setNom("Dupont");
        utilisateur.setPrenom("Marie");
        utilisateur.setEmail("marie.dupont@test.com");

        mission = new Mission();
        mission.setId(1L);
        mission.setTitre("Développement API REST");
        mission.setDescription("Création d'une API REST complète avec Spring Boot");
        mission.setDateDebut(LocalDate.of(2026, 1, 15));
        mission.setDateFin(LocalDate.of(2026, 3, 15));
        mission.setStatut(StatutMission.PENDING);
        mission.setValidatorEmail("validateur@test.com");
        mission.setUtilisateur(utilisateur);
    }

    @Test
    @DisplayName(" Envoi d'invitation de validation — email envoyé avec succès")
    void sendValidationInvitation_shouldSendEmail() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendValidationInvitation(mission);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName(" Notification mission validée — email envoyé à l'utilisateur")
    void sendMissionValidatedNotification_shouldSendEmail() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendMissionValidatedNotification(mission);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName(" Notification mission rejetée — email envoyé avec motif")
    void sendMissionRejectedNotification_shouldSendEmailWithReason() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendMissionRejectedNotification(mission, "Compétences insuffisantes");

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName(" Notification mission rejetée — sans motif ne lève pas d'exception")
    void sendMissionRejectedNotification_withNullReason_shouldNotThrow() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendMissionRejectedNotification(mission, null);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName(" Erreur envoi email — ne lève pas d'exception (graceful)")
    void sendEmail_whenMailSenderFails_shouldNotThrow() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new org.springframework.mail.MailSendException("SMTP error")).when(mailSender).send(any(MimeMessage.class));

        // Ne doit pas lancer d'exception
        emailService.sendValidationInvitation(mission);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}


