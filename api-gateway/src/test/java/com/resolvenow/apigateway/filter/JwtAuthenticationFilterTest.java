package com.resolvenow.apigateway.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.resolvenow.apigateway.security.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private PrintWriter printWriter;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtUtil);
    }

    @Test
    void loginEndpoint_ShouldSkipJwtValidation() throws Exception {

        when(request.getRequestURI())
                .thenReturn("/auth/login");

        jwtAuthenticationFilter.doFilter(
                request,
                response,
                filterChain
        );

        verify(filterChain, times(1))
                .doFilter(request, response);

        verifyNoInteractions(jwtUtil);
    }

    @Test
    void requestWithoutJwt_ShouldReturn401() throws Exception {

        when(request.getRequestURI())
                .thenReturn("/complaints");

        when(request.getHeader("Authorization"))
                .thenReturn(null);

        when(response.getWriter())
                .thenReturn(printWriter);

        jwtAuthenticationFilter.doFilter(
                request,
                response,
                filterChain
        );

        verify(response, times(1))
                .setStatus(401);

        verify(filterChain, never())
                .doFilter(request, response);
    }

    @Test
    void requestWithInvalidJwt_ShouldReturn401() throws Exception {

        when(request.getRequestURI())
                .thenReturn("/complaints");

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer invalid-token");

        when(jwtUtil.validateToken("invalid-token"))
                .thenReturn(false);

        when(response.getWriter())
                .thenReturn(printWriter);

        jwtAuthenticationFilter.doFilter(
                request,
                response,
                filterChain
        );

        verify(response, times(1))
                .setStatus(401);

        verify(filterChain, never())
                .doFilter(request, response);
    }

    @Test
    void requestWithValidJwt_ShouldContinueFilterChain() throws Exception {

        when(request.getRequestURI())
                .thenReturn("/complaints");

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer valid-token");

        when(jwtUtil.validateToken("valid-token"))
                .thenReturn(true);

        when(jwtUtil.extractUsername("valid-token"))
                .thenReturn("testuser");

        when(jwtUtil.extractRole("valid-token"))
                .thenReturn("USER");

        jwtAuthenticationFilter.doFilter(
                request,
                response,
                filterChain
        );

        verify(jwtUtil, times(1))
                .validateToken("valid-token");

        verify(jwtUtil, times(1))
                .extractUsername("valid-token");

        verify(jwtUtil, times(1))
                .extractRole("valid-token");

        verify(filterChain, times(1))
                .doFilter(request, response);
    }
}