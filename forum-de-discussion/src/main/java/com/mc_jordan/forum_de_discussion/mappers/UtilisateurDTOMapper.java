package com.mc_jordan.forum_de_discussion.mappers;

import com.mc_jordan.forum_de_discussion.dto.UtilisateurDTO;
import com.mc_jordan.forum_de_discussion.entites.Utilisateur;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UtilisateurDTOMapper implements Function<Utilisateur, UtilisateurDTO> {
    @Override
    public UtilisateurDTO apply(Utilisateur utilisateur) {
        return new UtilisateurDTO(utilisateur.getId(), utilisateur.getNomUtilisateur(), utilisateur.getEmail(), utilisateur.getNomEtPrenom(),utilisateur.getRole(), utilisateur.getPhotoProfil(), utilisateur.isEstVerifier());
    }
}
