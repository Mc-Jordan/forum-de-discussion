package com.mc_jordan.forum_de_discussion.services;

import com.mc_jordan.forum_de_discussion.entites.Utilisateur;
import com.mc_jordan.forum_de_discussion.entites.Validation;
import com.mc_jordan.forum_de_discussion.repositories.ValidationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class ValidationService {

    private  final ValidationRepository validationRepository;
    private  final EmailSenderService emailSenderService;


    public void enregistrer(Utilisateur utilisateur) {
        Validation validation = new Validation();
        validation.setUtilisateur(utilisateur);
        validation.setCreation(Instant.now());
        validation.setExpire(validation.getCreation().plus(10, ChronoUnit.MINUTES));
        Random r = new Random();
        int radomNumbr = r.nextInt(999999);
        validation.setCode(String.format("%06d", radomNumbr));
        validationRepository.save(validation);
        emailSenderService.envoyerEmail(validation);
    }

    public Validation lireEnFonctionDuCode(String code) {
        return validationRepository.findByCode(code).orElseThrow(()-> new RuntimeException("Ce code est inavlide"));
    }
}
