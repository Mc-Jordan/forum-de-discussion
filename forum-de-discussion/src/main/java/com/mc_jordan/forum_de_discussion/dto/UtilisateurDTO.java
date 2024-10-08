package com.mc_jordan.forum_de_discussion.dto;

import com.mc_jordan.forum_de_discussion.entites.Role;

public record UtilisateurDTO(
        int id,
        String nomUtilisateur,
        String email,
        String nomEtPrenom,
        Role role,
        String photoProfil,
        Boolean estVerifier

) {
}
