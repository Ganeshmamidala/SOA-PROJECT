package com.resolvenow.authservice.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "ResolveNowSuperSecretKeyForJwtAuthentication2026";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24; // 24 hours

    private final SecretKey key;

    public JwtUtil() {

        this.key = Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    // =========================
    // GENERATE JWT
    // =========================

    public String generateToken(String username, String role) {

        Date now = new Date();

        Date expiration =
                new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    // =========================
    // EXTRACT USERNAME
    // =========================

    public String extractUsername(String token) {

        return getClaims(token).getSubject();
    }

    // =========================
    // EXTRACT ROLE
    // =========================

    public String extractRole(String token) {

        return getClaims(token)
                .get("role", String.class);
    }

    // =========================
    // VALIDATE TOKEN
    // =========================

    public boolean validateToken(String token) {

        try {

            Claims claims = getClaims(token);

            return !claims
                    .getExpiration()
                    .before(new Date());

        } catch (Exception e) {

            return false;
        }
    }

    // =========================
    // GET JWT CLAIMS
    // =========================

    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}