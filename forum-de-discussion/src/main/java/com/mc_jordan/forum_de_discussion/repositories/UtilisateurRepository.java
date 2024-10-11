package com.mc_jordan.forum_de_discussion.repositories;

import com.mc_jordan.forum_de_discussion.entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    boolean existsByEmail(String email);

    boolean existsByNomUtilisateur(String nomUtilisateur);

    List<Utilisateur> findAllByEmail(String email);

    List<Utilisateur> findAllByNomUtilisateur(String nomUtilisateur);

    Optional<Utilisateur> findByNomUtilisateur(String nomUtilisateur);

    Optional<Utilisateur> findByEmail(String email);
}
