package com.resolvenow.apigateway.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.resolvenow.apigateway.security.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Public endpoints
        if (path.equals("/auth/login")
                || path.equals("/auth/register")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Get Authorization header
        String authorizationHeader =
                request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter()
                    .write("Missing or invalid JWT token");

            return;
        }

        // Extract token
        String token = authorizationHeader.substring(7);

        // Validate token
        if (!jwtUtil.validateToken(token)) {

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter()
                    .write("Invalid or expired JWT token");

            return;
        }

        // Extract username
        String username = jwtUtil.extractUsername(token);

        // Extract role
        String role = jwtUtil.extractRole(token);

        // Convert role into Spring Security authority
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + role);

        // Create authentication
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singletonList(authority)
                );

        // Store authentication
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        // Continue request
        filterChain.doFilter(request, response);
    }
}