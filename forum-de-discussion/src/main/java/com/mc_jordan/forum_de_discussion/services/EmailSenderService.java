package com.mc_jordan.forum_de_discussion.services;

import com.mc_jordan.forum_de_discussion.entites.Validation;
import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@AllArgsConstructor
@Service
public class EmailSenderService {
    private final JavaMailSender javaMailSender;

    //la methode pour envoi un mail de type texte
    public String envoyerEmail(Validation validation) {
        boolean emailStatut = this.isDomainValid(validation.getUtilisateur().getEmail());
        if (emailStatut) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
            try {
                messageHelper.setTo(validation.getUtilisateur().getEmail());
                messageHelper.setSubject("Code d'activation sur le forum de discussion de K48");
                messageHelper.setFrom("no-reply@Mcjordan.dev");
                messageHelper.setText("M./Mme/Mlle. " + validation.getUtilisateur().getNomEtPrenom() + ",\n\nVotre code d'activation est: " + validation.getCode() + ".\n " +
                        "Ce code expire dans 10 minutes. \n\n\n" +
                        "A Bientot!!! \n\n                                     McJordan");
                message.setHeader("Return-Receipt-To", "jordannandjo11@gmail.com");
                this.sentMail(message);
            } catch (MessagingException e) {
                throw new RuntimeException("Message non envoyé");
            }
            return "Email sent";
        }
        throw new RuntimeException("Nom de domain non valide");
    }

    public boolean sentMail(MimeMessage message) {
        try {
            // Envoi de l'email
            javaMailSender.send(message);
        } catch (MailSendException e) {
            // Récupération de la cause racine de l'erreur
            Throwable rootCause = e.getRootCause();
            if (rootCause instanceof SendFailedException) {
                SendFailedException sendFailedException = (SendFailedException) rootCause;
                // Vérifier si l'adresse email est invalide
                if (sendFailedException.getInvalidAddresses() != null && sendFailedException.getInvalidAddresses().length > 0) {
                    throw new RuntimeException("L'adresse email fournie est invalide : " + sendFailedException.getInvalidAddresses()[0]);
                }
            }
            // Gérer d'autres types d'erreurs d'envoi
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage(), e);
        }
        return true;
    }

    //on verifie si le nom de domaine est valide
    private boolean isDomainValid(String email) {
        String domain = email.substring(email.indexOf("@")+1);

        InetAddress adress = null;
        try {
            adress = InetAddress.getByName(domain);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Nom de domain non valide", e);
        }
        return adress !=null;
    }
}
