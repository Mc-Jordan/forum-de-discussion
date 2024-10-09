package com.mc_jordan.forum_de_discussion.entites;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "utilisateur")
public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String nomUtilisateur;

    @Column(nullable = false)
    private String nomEtPrenom;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    private boolean estVerifier = false;

    private String photoProfil;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE"+this.role.getLibelle()));
    }

    @Override
    public String getPassword() {
        return this.motDePasse;
    }

    @Override
    public String getUsername() {
        return this.nomUtilisateur;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.estVerifier;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.estVerifier;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.estVerifier;
    }

    @Override
    public boolean isEnabled() {
        return this.estVerifier;
    }
}
