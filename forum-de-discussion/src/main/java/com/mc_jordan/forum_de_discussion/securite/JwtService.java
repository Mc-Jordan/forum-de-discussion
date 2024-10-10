package com.mc_jordan.forum_de_discussion.securite;

import com.mc_jordan.forum_de_discussion.entites.Jwt;
import com.mc_jordan.forum_de_discussion.entites.Utilisateur;
import com.mc_jordan.forum_de_discussion.repositories.JwtRepository;
import com.mc_jordan.forum_de_discussion.services.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@AllArgsConstructor
@Service
public class JwtService {

    public static final String TOKEN = "token";
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    private UtilisateurService utilisateurService;
    private JwtRepository jwtRepository;
    private final String SECRET = "9D0EB6B1C2E1FAD0F53A248F6C3B5E4E2F6D8G3H1I0J7K4L1M9N2O3P5Q0R7S9T1U4V2W6X0Y3Z";

    public Map<String,String> generateToken(String nomUtilisateur) {
        Utilisateur utilisateur = (Utilisateur) utilisateurService.loadUserByUsername(nomUtilisateur);
        disableTokens(utilisateur);
        Map<String, String> jwtMap = generate(utilisateur);
        Jwt jwt = Jwt.
                builder()
                .token(jwtMap.get(TOKEN))
                .desactivate(false)
                .expire(false)
                .utilisateur(utilisateur).
                build();
        this.jwtRepository.save(jwt);
        return jwtMap;
    }

    private Map<String, String> generate(Utilisateur utilisateur) {

        final long  currenTime = System.currentTimeMillis();
        Map<String, Object> claims = Map.of(
                "nom", utilisateur.getNomEtPrenom(),
                "email", utilisateur.getEmail(),
                Claims.EXPIRATION, new Date(currenTime + 1000 * 60 * 30),
                Claims.SUBJECT, utilisateur.getNomUtilisateur()

        );
        final String token = Jwts.builder()
                .setIssuedAt(new Date(currenTime))
                .setExpiration(new Date(currenTime + 1000 * 60 * 30))
                .setSubject(utilisateur.getUsername())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of(
                TOKEN,token
        );
    }

    private Key getKey() {
        byte[] decode = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(decode);
    }

    public void disableTokens(Utilisateur utilisateur) {
        final List<Jwt> jwtList = jwtRepository.findUtilisateurNomUtilisateur(utilisateur.getNomUtilisateur()).peek(
                jwt -> {
                    jwt.setDesactivate(true);
                    jwt.setExpire(true);
                }
        ).collect(Collectors.toList());
        this.jwtRepository.saveAll(jwtList);
    }


    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }
    public Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public Boolean  validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Jwt getTokenByValue(String token) {
        return this.jwtRepository.findByTokenAndDesactivateAndExpire(
                token,
                        false,
                        false)
                .orElseThrow(() -> new RuntimeException("Token non retrouvé"));
    }

    public void deconnexion() {
        Utilisateur utilisateur =(Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = jwtRepository.findUtilisateurValidToken(utilisateur.getNomUtilisateur(), false, false)
                .orElseThrow(() -> new RuntimeException("Token non valide"));
        jwt.setDesactivate(true);
        jwt.setExpire(true);
        jwtRepository.save(jwt);
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void removeAllUseLessTokens() {
        jwtRepository.deleteAllByDesactivateAndExpire(true,true);
    }
}
