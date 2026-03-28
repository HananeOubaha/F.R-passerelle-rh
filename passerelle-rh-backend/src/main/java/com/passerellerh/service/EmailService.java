package com.passerellerh.service;

import com.passerellerh.entity.Mission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend-url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${spring.mail.username:noreply@passerelle-rh.com}")
    private String fromEmail;

    /**
     * Envoie une invitation de validation au validateur externe.
     */
    @Async
    public void sendValidationInvitation(Mission mission) {
        String to = mission.getValidatorEmail();
        String userName = mission.getUtilisateur().getPrenom() + " " + mission.getUtilisateur().getNom();
        String subject = "Passerelle RH — Nouvelle mission à valider : " + mission.getTitre();

        String htmlBody = """
                <div style="font-family: 'Segoe UI', Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                    <div style="background: linear-gradient(135deg, #059669, #10B981); padding: 30px; border-radius: 12px 12px 0 0; text-align: center;">
                        <h1 style="color: white; margin: 0; font-size: 24px;">🎯 Passerelle RH</h1>
                        <p style="color: #D1FAE5; margin: 8px 0 0 0;">Plateforme de validation de compétences</p>
                    </div>
                    <div style="background: #fff; padding: 30px; border: 1px solid #E5E7EB; border-top: none;">
                        <h2 style="color: #1F2937; margin-top: 0;">Nouvelle mission à valider</h2>
                        <p style="color: #4B5563;">Bonjour,</p>
                        <p style="color: #4B5563;"><strong>%s</strong> vous a désigné comme validateur pour la mission suivante :</p>
                        <div style="background: #F9FAFB; border-left: 4px solid #10B981; padding: 16px; margin: 20px 0; border-radius: 0 8px 8px 0;">
                            <h3 style="color: #1F2937; margin: 0 0 8px 0;">%s</h3>
                            <p style="color: #6B7280; margin: 0;">%s</p>
                            <p style="color: #9CA3AF; margin: 8px 0 0 0; font-size: 13px;">Du %s au %s</p>
                        </div>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s/validateur"
                               style="background: #10B981; color: white; padding: 14px 32px; border-radius: 8px;
                                      text-decoration: none; font-weight: bold; font-size: 15px; display: inline-block;">
                                 Accéder au dashboard validateur
                            </a>
                        </div>
                        <p style="color: #9CA3AF; font-size: 13px; text-align: center;">
                            Si vous n'avez pas de compte, inscrivez-vous avec cet email pour voir la mission.
                        </p>
                    </div>
                    <div style="background: #F9FAFB; padding: 16px; border-radius: 0 0 12px 12px; text-align: center; border: 1px solid #E5E7EB; border-top: none;">
                        <p style="color: #9CA3AF; font-size: 12px; margin: 0;">© 2026 Passerelle RH — Plateforme de compétences professionnelles</p>
                    </div>
                </div>
                """.formatted(
                userName,
                mission.getTitre(),
                mission.getDescription() != null ? mission.getDescription() : "Aucune description",
                mission.getDateDebut() != null ? mission.getDateDebut().toString() : "N/A",
                mission.getDateFin() != null ? mission.getDateFin().toString() : "N/A",
                frontendUrl
        );

        sendHtmlEmail(to, subject, htmlBody);
    }

    /**
     * Notifie l'utilisateur que sa mission a été validée.
     */
    @Async
    public void sendMissionValidatedNotification(Mission mission) {
        String to = mission.getUtilisateur().getEmail();
        String subject = " Mission validée : " + mission.getTitre();

        String htmlBody = """
                <div style="font-family: 'Segoe UI', Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                    <div style="background: linear-gradient(135deg, #059669, #10B981); padding: 30px; border-radius: 12px 12px 0 0; text-align: center;">
                        <h1 style="color: white; margin: 0;"> Passerelle RH</h1>
                    </div>
                    <div style="background: #fff; padding: 30px; border: 1px solid #E5E7EB; border-top: none;">
                        <h2 style="color: #059669;"> Bonne nouvelle !</h2>
                        <p style="color: #4B5563;">Bonjour <strong>%s</strong>,</p>
                        <p style="color: #4B5563;">Votre mission <strong>« %s »</strong> a été <strong style="color: #059669;">validée avec succès</strong> !</p>
                        <p style="color: #4B5563;">Consultez votre Passeport RH pour voir votre score et vos nouveaux badges.</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s/passeport"
                               style="background: #10B981; color: white; padding: 14px 32px; border-radius: 8px;
                                      text-decoration: none; font-weight: bold;">
                                 Voir mon Passeport RH
                            </a>
                        </div>
                    </div>
                </div>
                """.formatted(
                mission.getUtilisateur().getPrenom(),
                mission.getTitre(),
                frontendUrl
        );

        sendHtmlEmail(to, subject, htmlBody);
    }

    /**
     * Notifie l'utilisateur que sa mission a été rejetée.
     */
    @Async
    public void sendMissionRejectedNotification(Mission mission, String reason) {
        String to = mission.getUtilisateur().getEmail();
        String subject = " Mission rejetée : " + mission.getTitre();

        String reasonBlock = (reason != null && !reason.isBlank())
                ? "<p style=\"color: #6B7280;\"><strong>Motif :</strong> " + reason + "</p>"
                : "";

        String htmlBody = """
                <div style="font-family: 'Segoe UI', Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                    <div style="background: linear-gradient(135deg, #DC2626, #EF4444); padding: 30px; border-radius: 12px 12px 0 0; text-align: center;">
                        <h1 style="color: white; margin: 0;"> Passerelle RH</h1>
                    </div>
                    <div style="background: #fff; padding: 30px; border: 1px solid #E5E7EB; border-top: none;">
                        <h2 style="color: #DC2626;"> Mission rejetée</h2>
                        <p style="color: #4B5563;">Bonjour <strong>%s</strong>,</p>
                        <p style="color: #4B5563;">Votre mission <strong>« %s »</strong> a été rejetée par le validateur.</p>
                        %s
                        <p style="color: #4B5563;">Vous pouvez corriger et soumettre une nouvelle mission.</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s/dashboard"
                               style="background: #6366F1; color: white; padding: 14px 32px; border-radius: 8px;
                                      text-decoration: none; font-weight: bold;">
                                 Retour au dashboard
                            </a>
                        </div>
                    </div>
                </div>
                """.formatted(
                mission.getUtilisateur().getPrenom(),
                mission.getTitre(),
                reasonBlock,
                frontendUrl
        );

        sendHtmlEmail(to, subject, htmlBody);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info(" Email envoyé avec succès à {}", to);
        } catch (Exception e) {
            log.error(" Erreur envoi email à {} : {}", to, e.getMessage());
        }
    }
}


