package com.resolvenow.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resolvenow.authservice.entity.User;
import com.resolvenow.authservice.security.JwtUtil;
import com.resolvenow.authservice.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    // =========================
    // REGISTER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        try {

            User savedUser = authService.register(user);

            // Do not return the password
            savedUser.setPassword(null);

            return ResponseEntity.ok(savedUser);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {

            User user = authService.findByUsername(
                    request.getUsername()
            );

            // Check password
            if (!authService.passwordMatches(
                    request.getPassword(),
                    user.getPassword())) {

                return ResponseEntity
                        .status(401)
                        .body("Invalid username or password");
            }

            // Generate JWT with username + role
            String token = jwtUtil.generateToken(
                    user.getUsername(),
                    user.getRole()
            );

            return ResponseEntity.ok(
                    new LoginResponse(
                            "Login successful",
                            token
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid username or password");
        }
    }

    // =========================
    // LOGIN REQUEST
    // =========================

    public static class LoginRequest {

        private String username;
        private String password;

        public LoginRequest() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // =========================
    // LOGIN RESPONSE
    // =========================

    public static class LoginResponse {

        private String message;
        private String token;

        public LoginResponse(String message, String token) {
            this.message = message;
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        public String getToken() {
            return token;
        }
    }
}