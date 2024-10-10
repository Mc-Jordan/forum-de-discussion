package com.mc_jordan.forum_de_discussion.entites;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jwt")
public class Jwt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;
    private boolean desactivate ;
    private boolean expire ;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private Utilisateur utilisateur;




}
