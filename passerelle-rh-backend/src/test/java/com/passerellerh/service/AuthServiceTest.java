package com.passerellerh.service;

import com.passerellerh.dto.AuthRequest;
import com.passerellerh.dto.AuthResponse;
import com.passerellerh.dto.RegisterRequest;
import com.passerellerh.entity.Utilisateur;
import com.passerellerh.enums.Role;
import com.passerellerh.repository.UserRepository;
import com.passerellerh.security.CustomUserDetailsService;
import com.passerellerh.security.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests - AuthService")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("✅ Inscription réussie — retourne un token JWT")
    void register_success_returnsAuthResponse() {
        // GIVEN
        RegisterRequest request = new RegisterRequest();
        request.setNom("Dupont");
        request.setPrenom("Jean");
        request.setEmail("jean@test.com");
        request.setPassword("password123");
        request.setVille("Casablanca");
        request.setRole(Role.USER);

        when(userRepository.existsByEmail("jean@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed_password");
        when(userRepository.save(any())).thenAnswer(i -> {
            Utilisateur u = (Utilisateur) i.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserDetails mockDetails = new org.springframework.security.core.userdetails.User(
                "jean@test.com", "hashed", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(userDetailsService.loadUserByUsername("jean@test.com")).thenReturn(mockDetails);
        when(jwtUtil.generateToken(any())).thenReturn("access_token");
        when(jwtUtil.generateRefreshToken(any())).thenReturn("refresh_token");

        // WHEN
        AuthResponse response = authService.register(request);

        // THEN
        assertNotNull(response);
        assertEquals("jean@test.com", response.getEmail());
        assertEquals("access_token", response.getAccessToken());
        assertEquals("USER", response.getRole());
        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("❌ Inscription — email déjà existant lève une exception")
    void register_emailAlreadyExists_throwsException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@test.com");

        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.register(request));

        assertEquals("Email already exists", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("✅ Connexion réussie — retourne un token JWT")
    void login_success_returnsAuthResponse() {
        // GIVEN
        AuthRequest request = new AuthRequest();
        request.setEmail("jean@test.com");
        request.setPassword("password123");

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setEmail("jean@test.com");
        utilisateur.setRole(Role.USER);

        UserDetails mockDetails = new org.springframework.security.core.userdetails.User(
                "jean@test.com", "hashed", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userDetailsService.loadUserByUsername("jean@test.com")).thenReturn(mockDetails);
        when(userRepository.findByEmail("jean@test.com")).thenReturn(Optional.of(utilisateur));
        when(jwtUtil.generateToken(any())).thenReturn("access_token");
        when(jwtUtil.generateRefreshToken(any())).thenReturn("refresh_token");

        // WHEN
        AuthResponse response = authService.login(request);

        // THEN
        assertNotNull(response);
        assertEquals("jean@test.com", response.getEmail());
        assertEquals("USER", response.getRole());
    }

    @Test
    @DisplayName("❌ Connexion — mauvais mot de passe lève une exception")
    void login_badCredentials_throwsException() {
        AuthRequest request = new AuthRequest();
        request.setEmail("jean@test.com");
        request.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

    @Test
    @DisplayName("✅ Refresh token valide — retourne de nouveaux tokens")
    void refreshToken_validToken_returnsNewTokens() {
        String refreshToken = "valid_refresh";

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setEmail("jean@test.com");
        utilisateur.setRole(Role.USER);

        UserDetails mockDetails = new org.springframework.security.core.userdetails.User(
                "jean@test.com", "hashed", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        when(jwtUtil.extractUsername(refreshToken)).thenReturn("jean@test.com");
        when(userDetailsService.loadUserByUsername("jean@test.com")).thenReturn(mockDetails);
        when(jwtUtil.validateToken(refreshToken, mockDetails)).thenReturn(true);
        when(userRepository.findByEmail("jean@test.com")).thenReturn(Optional.of(utilisateur));
        when(jwtUtil.generateToken(mockDetails)).thenReturn("new_access_token");
        when(jwtUtil.generateRefreshToken(mockDetails)).thenReturn("new_refresh_token");

        AuthResponse response = authService.refreshToken(refreshToken);

        assertNotNull(response);
        assertEquals("new_access_token", response.getAccessToken());
        assertEquals("new_refresh_token", response.getRefreshToken());
        assertEquals("jean@test.com", response.getEmail());
    }

    @Test
    @DisplayName("❌ Refresh token invalide — lève une exception")
    void refreshToken_invalidToken_throwsException() {
        String refreshToken = "invalid_refresh";

        UserDetails mockDetails = new org.springframework.security.core.userdetails.User(
                "jean@test.com", "hashed", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        when(jwtUtil.extractUsername(refreshToken)).thenReturn("jean@test.com");
        when(userDetailsService.loadUserByUsername("jean@test.com")).thenReturn(mockDetails);
        when(jwtUtil.validateToken(refreshToken, mockDetails)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.refreshToken(refreshToken));
        assertEquals("Invalid refresh token", ex.getMessage());
    }
}
