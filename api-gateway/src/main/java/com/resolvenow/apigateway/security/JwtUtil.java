package com.resolvenow.apigateway.security;

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

    private final SecretKey key;

    public JwtUtil() {

        this.key = Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
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
    // EXTRACT USERNAME
    // =========================

    public String extractUsername(String token) {

        return getClaims(token)
                .getSubject();
    }

    // =========================
    // EXTRACT ROLE
    // =========================

    public String extractRole(String token) {

        return getClaims(token)
                .get("role", String.class);
    }

    // =========================
    // GET CLAIMS
    // =========================

    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}