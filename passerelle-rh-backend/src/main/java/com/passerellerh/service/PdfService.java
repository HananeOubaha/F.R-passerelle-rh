package com.passerellerh.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.passerellerh.entity.Mission;
import com.passerellerh.entity.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfService {

    public byte[] generateAttestation(Mission mission, Validation validation, String qrCodeData) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            // Font definitions
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font highlightFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

            // Title
            Paragraph title = new Paragraph("ATTESTATION DE MISSION", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(30);
            document.add(title);

            // Intro
            Paragraph intro = new Paragraph(
                    "L'équipe Passerelle RH certifie par la présente que :", normalFont);
            intro.setSpacingAfter(10);
            document.add(intro);

            // User info
            String fullName = mission.getUtilisateur().getPrenom() + " " + mission.getUtilisateur().getNom();
            Paragraph userName = new Paragraph(fullName, headerFont);
            userName.setAlignment(Paragraph.ALIGN_CENTER);
            userName.setSpacingAfter(20);
            document.add(userName);

            // Mission info
            document.add(new Paragraph("A accompli avec succès la mission suivante :", normalFont));
            document.add(new Paragraph("Titre : " + mission.getTitre(), highlightFont));
            document.add(new Paragraph("Du " + mission.getDateDebut() + " au " + mission.getDateFin(), normalFont));
            document.add(new Paragraph("Description : " + mission.getDescription(), normalFont));

            // Scores
            Paragraph scoreHeader = new Paragraph("Évaluation globale", headerFont);
            scoreHeader.setSpacingBefore(20);
            scoreHeader.setSpacingAfter(10);
            document.add(scoreHeader);

            if (validation.getScores() != null && !validation.getScores().isEmpty()) {
                for (com.passerellerh.entity.CompetenceScore score : validation.getScores()) {
                    document.add(new Paragraph(score.getCompetence().getNom() + " : " + score.getScore() + "/5",
                            normalFont));
                }
            } else {
                document.add(new Paragraph("Aucune évaluation détaillée disponible.", normalFont));
            }

            if (validation.getCommentaire() != null && !validation.getCommentaire().isEmpty()) {
                document.add(new Paragraph("Commentaire du validateur : " + validation.getCommentaire(),
                        FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12)));
            }

            // Validated by
            String validateurName = validation.getValidateur().getPrenom() + " " + validation.getValidateur().getNom();
            Paragraph validatorInfo = new Paragraph("Mission validée par : " + validateurName, normalFont);
            validatorInfo.setSpacingBefore(30);
            validatorInfo.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(validatorInfo);

            Paragraph dateValid = new Paragraph("Fait le : " + validation.getDateValidation().toLocalDate(),
                    normalFont);
            dateValid.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(dateValid);

            // QR Code Generation
            if (qrCodeData != null && !qrCodeData.isEmpty()) {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 150, 150);

                ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
                byte[] pngData = pngOutputStream.toByteArray();

                Image qrImage = Image.getInstance(pngData);
                qrImage.setAlignment(Image.ALIGN_CENTER);
                qrImage.setSpacingBefore(40);
                document.add(qrImage);

                Paragraph authText = new Paragraph("Scannez ce QR Code pour vérifier l'authenticité",
                        FontFactory.getFont(FontFactory.HELVETICA, 8));
                authText.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(authText);
            }

            document.close();
            return out.toByteArray();
        }
    }
}
