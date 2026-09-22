package com.resolvenow.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.resolvenow.apigateway.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // =========================
                // PUBLIC
                // =========================

                .requestMatchers(
                        "/auth/login",
                        "/auth/register"
                ).permitAll()

                // =========================
                // COMPLAINTS
                // =========================

                .requestMatchers(
                        org.springframework.http.HttpMethod.GET,
                        "/complaints/**"
                ).hasAnyRole("USER", "ADMIN")

                .requestMatchers(
                        org.springframework.http.HttpMethod.POST,
                        "/complaints"
                ).hasAnyRole("USER", "ADMIN")

                .requestMatchers(
                        "/complaints/**"
                ).hasRole("ADMIN")

                // =========================
                // ASSIGNMENTS
                // =========================

                .requestMatchers(
                        "/assignments/**"
                ).hasRole("ADMIN")

                // =========================
                // NOTIFICATIONS
                // =========================

                .requestMatchers(
                        "/notifications/**"
                ).hasAnyRole("USER", "ADMIN")

                // =========================
                // EVERYTHING ELSE
                // =========================

                .anyRequest().authenticated()
            )

            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}