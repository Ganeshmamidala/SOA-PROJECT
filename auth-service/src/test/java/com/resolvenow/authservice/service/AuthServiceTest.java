package com.resolvenow.authservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.resolvenow.authservice.entity.User;
import com.resolvenow.authservice.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();

        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole("USER");
    }

    @Test
    void register_ShouldSaveUserWithEncodedPassword() {

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        User result = authService.register(user);

        assertNotNull(result);

        assertEquals(
                "encodedPassword",
                user.getPassword()
        );

        assertEquals(
                "USER",
                user.getRole()
        );

        verify(passwordEncoder, times(1))
                .encode("password123");

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void register_ShouldSetDefaultRole_WhenRoleIsMissing() {

        user.setRole(null);

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        authService.register(user);

        assertEquals(
                "USER",
                user.getRole()
        );
    }

    @Test
    void register_ShouldThrowException_WhenUsernameExists() {

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        assertThrows(
                RuntimeException.class,
                () -> authService.register(user)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        assertThrows(
                RuntimeException.class,
                () -> authService.register(user)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void findByUsername_ShouldReturnUser() {

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        User result =
                authService.findByUsername("testuser");

        assertNotNull(result);

        assertEquals(
                "testuser",
                result.getUsername()
        );

        assertEquals(
                "USER",
                result.getRole()
        );
    }

    @Test
    void passwordMatches_ShouldReturnTrueForCorrectPassword() {

        when(passwordEncoder.matches(
                "password123",
                "encodedPassword"
        )).thenReturn(true);

        boolean result =
                authService.passwordMatches(
                        "password123",
                        "encodedPassword"
                );

        assertTrue(result);

        verify(passwordEncoder, times(1))
                .matches(
                        "password123",
                        "encodedPassword"
                );
    }
}