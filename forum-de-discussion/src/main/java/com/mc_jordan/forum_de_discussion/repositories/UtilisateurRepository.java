package com.mc_jordan.forum_de_discussion.repositories;

import com.mc_jordan.forum_de_discussion.entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    boolean existsByEmail(String email);

    boolean existsByNomUtilisateur(String nomUtilisateur);

    List<Utilisateur> findAllByEmail(String email);

    List<Utilisateur> findAllByNomUtilisateur(String nomUtilisateur);
}
