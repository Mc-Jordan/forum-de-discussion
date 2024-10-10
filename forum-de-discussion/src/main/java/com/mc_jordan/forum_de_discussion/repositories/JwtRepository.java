package com.mc_jordan.forum_de_discussion.repositories;

import com.mc_jordan.forum_de_discussion.entites.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;


public interface JwtRepository extends JpaRepository<Jwt, Integer> {

    @Query("FROM Jwt j WHERE j.expire= :expire AND j.desactivate= :desactivate AND j.utilisateur.nomUtilisateur = :nomUtilisateur")
    Optional<Jwt> findUtilisateurValidToken(String nomUtilisateur, boolean desactivate, boolean expire);

    @Query("FROM Jwt j WHERE  j.utilisateur.nomUtilisateur = :nomUtilisateur")
    Stream<Jwt> findUtilisateurNomUtilisateur(String nomUtilisateur);

    Optional<Jwt> findByTokenAndDesactivateAndExpire(String token, boolean desactivate, boolean expire);

    void deleteAllByDesactivateAndExpire(boolean desactivate, boolean expire);
}
