package com.gocamp.reservecamping.jwt;

import com.gocamp.reservecamping.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    // =====================================
    // CLÉ SECRÈTE (HS512) — VRAIMENT 512 bits
    // =====================================
    private static final String RAW_SECRET =
            "KJHGFDSQAZPOIUYTREWMLKJHGFDSAQQWERTYUIOPMLKJHGFDSAZXCVBNMLPOIUYT"; 
    // 64 caractères = 512 bits, parfait pour HS512

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // On encode en Base64 pour être 100% compatible KeySpec
        byte[] decoded = Base64.getEncoder().encode(RAW_SECRET.getBytes(StandardCharsets.UTF_8));
        this.secretKey = Keys.hmacShaKeyFor(decoded);
    }

    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 24; // 24h


    // ========================
    //   GENERATION DU TOKEN
    // ========================
    public String generateToken(String email) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    // =========================
    //   EXTRAIRE Email (Subject)
    // =========================
    public String extractEmail(String token) {
        return parse(token).getPayload().getSubject();
    }

    // =========================
    //   VALIDATION DU TOKEN
    // =========================
    public boolean isTokenValid(String token, UserDetailsImpl userDetails) {

        String email = extractEmail(token);
        boolean sameUser = email.equals(userDetails.getUsername());
        boolean notExpired = !isTokenExpired(token);

        return sameUser && notExpired;
    }

    public boolean isTokenExpired(String token) {
        return parse(token).getPayload().getExpiration().before(new Date());
    }

    // =========================
    //   PARSE + VERIFICATION
    // =========================
    private Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }
}
