package com.mc_jordan.forum_de_discussion.services;

import com.mc_jordan.forum_de_discussion.dto.UtilisateurDTO;
import com.mc_jordan.forum_de_discussion.entites.Utilisateur;
import com.mc_jordan.forum_de_discussion.entites.Validation;
import com.mc_jordan.forum_de_discussion.mappers.UtilisateurDTOMapper;
import com.mc_jordan.forum_de_discussion.repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;


@AllArgsConstructor
@Service
public class UtilisateurService implements UserDetailsService {

    private PasswordEncoder bCryptPasswordEncoder;
    private UtilisateurDTOMapper utilisateurDTOMapper;
    private UtilisateurRepository utilisateurRepository;
    private ValidationService validationService;




    //methode permetant l'insciption d'un utilisateur
    @Transactional
    public UtilisateurDTO createUtilisateur(Utilisateur utilisateur) {
        //si l'mail existe deja, on leve une exception
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            throw new RuntimeException("cette addresse est deja associé au un utilisateur");
        }else  if (utilisateurRepository.existsByNomUtilisateur(utilisateur.getNomUtilisateur())){
            throw new RuntimeException("ce nom d'utilisateur est deja associé à un utilisateur");
        }
            utilisateur.setMotDePasse(this.bCryptPasswordEncoder.encode(utilisateur.getPassword()));
            utilisateur=utilisateurRepository.save(utilisateur);
            validationService.enregistrer(utilisateur);
        return utilisateurDTOMapper.apply(utilisateur);
    }

    //methode permetant de modifier un utilisateur
    public UtilisateurDTO updateUtilisateur(Utilisateur utilisateur, int id) {
        Utilisateur utilisateurFind = utilisateurRepository.findById(id).orElseThrow(()-> new RuntimeException("Cet Utilisateur n'existe pas"));
        if (utilisateurFind != null) {
            //on s'assure qu'une meme addresse mail ne figure pas deux fois dans la base de données
            if ((utilisateurRepository.findAllByEmail(utilisateur.getEmail()).size() == 1 && utilisateur.getEmail().equals(utilisateurFind.getEmail()))
            || (utilisateurRepository.findAllByEmail(utilisateur.getEmail()).isEmpty() && !utilisateur.getEmail().equals(utilisateurFind.getEmail())) ){
                //on s'assure qu'une meme addresse mail ne figure pas deux fois dans la base de données
                if (((utilisateurRepository.findAllByNomUtilisateur(utilisateur.getUsername()).size() == 1 && utilisateur.getUsername().equals(utilisateurFind.getUsername()))
                        || (utilisateurRepository.findAllByNomUtilisateur(utilisateur.getUsername()).isEmpty() && !utilisateur.getUsername().equals(utilisateurFind.getUsername())) )){
                    if (utilisateur.getEmail()!=null){
                        utilisateurFind.setEmail(utilisateur.getEmail());
                    }
                    if (utilisateur.getNomUtilisateur()!=null){
                        utilisateurFind.setNomUtilisateur(utilisateur.getUsername());
                    }
                    if (utilisateur.getRole()!=null){
                        utilisateurFind.setRole(utilisateur.getRole());
                    }
                    if (utilisateur.getNomEtPrenom()!=null){
                        utilisateurFind.setNomEtPrenom(utilisateur.getNomEtPrenom());
                    }
                    return utilisateurDTOMapper.apply(utilisateurRepository.save(utilisateurFind));
                }else {
                    throw  new RuntimeException("ce nom d'utilisateur n'est pas disponible");
                }
            }else {
                throw  new RuntimeException("cette addresse es deje attribué a un utilisateur");
            }
        }else {
            throw new RuntimeException("Cet Utilisateur n'existe pas");
        }
    }


    public UtilisateurDTO activerCompteUtilisateur(Map<String, String> activation) {
        String code = activation.get("code");
        Utilisateur utilisateur;
        Validation validation = validationService.lireEnFonctionDuCode(code);

        if (validation==null){

            throw new RuntimeException("Ce code est inavlide");
        }
        else if (validation.getExpire().isBefore(Instant.now())) {
            throw new RuntimeException("Votre code a expiré");
        }else {
            utilisateur = utilisateurRepository.findById(validation.getUtilisateur().getId()).orElseThrow(()-> new RuntimeException("Cette Utilisateur n'existe pas"));
            utilisateur.setEstVerifier(true);

        }
        return utilisateurDTOMapper.apply(this.utilisateurRepository.save(utilisateur));
    }

    @Override
    public Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur=this.utilisateurRepository.findByNomUtilisateur(username).orElseThrow(()-> new UsernameNotFoundException("Cette Utilisateur n'existe pas"));
        return utilisateur;
    }

    public UtilisateurDTO demandeDeModificationDeMotDePasse(Map<String, String> parametres) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(parametres.get("email"))
                .orElseThrow(() -> new RuntimeException("Cette Utilisateur n'existe pas"));
        this.validationService.enregistrer(utilisateur);
        return utilisateurDTOMapper.apply(utilisateur);
    }

    public UtilisateurDTO modifierMotDePasse(Map<String, String> parametres) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(parametres.get("email"))
                .orElseThrow(() -> new RuntimeException("Cette Utilisateur n'existe pas"));
        String code = parametres.get("code");
        Validation validation = validationService.lireEnFonctionDuCode(code);
        if (validation==null){
            throw new RuntimeException("Ce code est inavlide");
        }
        else if (validation.getExpire().isBefore(Instant.now())) {
            throw new RuntimeException("Votre code a expiré");
        }else if(!validation.getUtilisateur().getEmail().equals(utilisateur.getEmail())){
            throw new RuntimeException("Inforrmations ambigues");
        }
            String password = this.bCryptPasswordEncoder.encode(parametres.get("password"));
            utilisateur.setMotDePasse(password);
            return utilisateurDTOMapper.apply(this.utilisateurRepository.save(utilisateur));


    }
}
