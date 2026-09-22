package com.resolvenow.authservice.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void generateToken_ShouldCreateValidToken() {

        String token =
                jwtUtil.generateToken("testuser", "USER");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {

        String token =
                jwtUtil.generateToken("testuser", "USER");

        String username =
                jwtUtil.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    void extractRole_ShouldReturnCorrectRole() {

        String token =
                jwtUtil.generateToken("testuser", "USER");

        String role =
                jwtUtil.extractRole(token);

        assertEquals("USER", role);
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {

        String token =
                jwtUtil.generateToken("testuser", "USER");

        boolean result =
                jwtUtil.validateToken(token);

        assertTrue(result);
    }

    @Test
    void validateToken_ShouldReturnFalseForInvalidToken() {

        String invalidToken =
                "this.is.not.a.valid.jwt";

        boolean result =
                jwtUtil.validateToken(invalidToken);

        assertFalse(result);
    }

    @Test
    void generateToken_ShouldSupportAdminRole() {

        String token =
                jwtUtil.generateToken("admin", "ADMIN");

        assertNotNull(token);

        assertEquals(
                "admin",
                jwtUtil.extractUsername(token)
        );

        assertEquals(
                "ADMIN",
                jwtUtil.extractRole(token)
        );

        assertTrue(
                jwtUtil.validateToken(token)
        );
    }
}