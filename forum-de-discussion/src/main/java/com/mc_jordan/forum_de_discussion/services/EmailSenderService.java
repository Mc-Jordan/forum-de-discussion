package com.mc_jordan.forum_de_discussion.services;

import com.mc_jordan.forum_de_discussion.entites.Validation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailSenderService {
    private final JavaMailSender javaMailSender;

    //la methode pour envoi un mail de type texte
    public String envoyerEmail(Validation validation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(validation.getUtilisateur().getEmail());
        message.setSubject("Code d'activation sur le forum de discussion de K48");
        message.setFrom("no-reply@Mcjordan.dev");

        message.setText("M./Mme/Mlle. "+validation.getUtilisateur().getNomEtPrenom()+",\n\nVotre code d'activation est: "+validation.getCode()+".\n " +
                "Ce code expire dans 10 minutes. \n\n\n" +
                "A Bientot!!! \n\n                                     McJordan");
        javaMailSender.send(message);
        return "Email sent";
    }
}
