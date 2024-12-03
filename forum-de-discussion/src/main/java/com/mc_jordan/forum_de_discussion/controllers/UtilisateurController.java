package com.mc_jordan.forum_de_discussion.controllers;

import com.mc_jordan.forum_de_discussion.dto.AuthentificationDTO;
import com.mc_jordan.forum_de_discussion.dto.UtilisateurDTO;
import com.mc_jordan.forum_de_discussion.entites.Utilisateur;
import com.mc_jordan.forum_de_discussion.securite.JwtService;
import com.mc_jordan.forum_de_discussion.services.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class UtilisateurController {
    private  UtilisateurService utilisateurService;
    private  AuthenticationManager authenticationManager;
    private JwtService jwtService;

    @PostMapping("/inscription")
    public ResponseEntity<UtilisateurDTO> creerUtilisateur(@RequestBody Utilisateur utilisateur) {
        return new ResponseEntity<>(utilisateurService.createUtilisateur(utilisateur), HttpStatus.CREATED);
    }

    @PostMapping("/connexion")
    public ResponseEntity<Map<String,String>> seConnecter(@RequestBody AuthentificationDTO authentificationDTO) {
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (authentificationDTO.nomUtilisateur(), authentificationDTO.motDePasse())
            );
        } catch (AuthenticationException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
        if (authenticate.isAuthenticated()) {
            return new ResponseEntity<>(jwtService.generateToken(authentificationDTO.nomUtilisateur()), HttpStatus.OK);
        }
        return null;
    }

    @PostMapping("/activation")
    public ResponseEntity<UtilisateurDTO> activation(@RequestBody Map<String,String> activation) {
        return new ResponseEntity<>(utilisateurService.activerCompteUtilisateur(activation), HttpStatus.CREATED);
    }

    @PostMapping("/deconnexion")
    public void deconnexion() {
        jwtService.deconnexion();
    }

    @PostMapping("/demande-modification-de-mot-de-passe")
    public ResponseEntity<UtilisateurDTO> demandeDeModificationDeMotDePasse(@RequestBody Map<String,String> email) {
        return new ResponseEntity<>(utilisateurService.demandeDeModificationDeMotDePasse(email), HttpStatus.CREATED);
    }


    @PutMapping("/modifier-mot-de-passe")
    public ResponseEntity<UtilisateurDTO> modifierMotDePasse(@RequestBody Map<String,String> parametre) {
        return new ResponseEntity<>(utilisateurService.modifierMotDePasse(parametre), HttpStatus.CREATED);
    }

    @PutMapping("/modifier-info/{id}")
    public ResponseEntity<UtilisateurDTO> update(@RequestBody Utilisateur utilisateur,@PathVariable int id) {
        return new ResponseEntity<>(utilisateurService.updateUtilisateur(utilisateur,id), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/hello")
    public  String getString(){
        return  "hello world";
    }
}