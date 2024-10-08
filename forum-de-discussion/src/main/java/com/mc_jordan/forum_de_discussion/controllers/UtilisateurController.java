package com.mc_jordan.forum_de_discussion.controllers;

import com.mc_jordan.forum_de_discussion.dto.UtilisateurDTO;
import com.mc_jordan.forum_de_discussion.entites.Utilisateur;
import com.mc_jordan.forum_de_discussion.services.UtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    @PostMapping("/inscription")
    public ResponseEntity<UtilisateurDTO> create(@RequestBody Utilisateur utilisateur) {
        return new ResponseEntity<>(utilisateurService.createUtilisateur(utilisateur), HttpStatus.CREATED);
    }

    @PostMapping("/activation")
    public ResponseEntity<UtilisateurDTO> activation(@RequestBody Map<String,String> activation) {
        return new ResponseEntity<>(utilisateurService.activerCompteUtilisateur(activation), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> update(@RequestBody Utilisateur utilisateur,@PathVariable int id) {
        return new ResponseEntity<>(utilisateurService.updateUtilisateur(utilisateur,id), HttpStatus.OK);
    }
}
