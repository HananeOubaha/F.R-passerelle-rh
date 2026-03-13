package com.passerellerh.service;

import com.passerellerh.dto.AuthRequest;
import com.passerellerh.dto.AuthResponse;
import com.passerellerh.dto.RegisterRequest;
import com.passerellerh.entity.Administrateur;
import com.passerellerh.entity.PasseportRH;
import com.passerellerh.entity.User;
import com.passerellerh.entity.Utilisateur;
import com.passerellerh.entity.Validateur;
import com.passerellerh.enums.Role;
import com.passerellerh.repository.UserRepository;
import com.passerellerh.security.CustomUserDetailsService;
import com.passerellerh.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;
        private final AuthenticationManager authenticationManager;
        private final CustomUserDetailsService userDetailsService;

        @Transactional
        public AuthResponse register(RegisterRequest request) {
                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new RuntimeException("Email already exists");
                }

                User user;
                if (request.getRole() == Role.ADMIN) {
                        user = new Administrateur();
                } else if (request.getRole() == Role.VALIDATOR) {
                        user = new Validateur();
                } else {
                        Utilisateur utilisateur = new Utilisateur();
                        // Initialize Passport only for regular users
                        PasseportRH passeport = new PasseportRH();
                        passeport.setUtilisateur(utilisateur);
                        utilisateur.setPasseport(passeport);
                        user = utilisateur;
                }

                user.setNom(request.getNom());
                user.setPrenom(request.getPrenom());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setVille(request.getVille());
                user.setRole(request.getRole() != null ? request.getRole() : Role.USER);
                user.setScoreFiabilite(0.0);
                user.setProfilePublicToken(UUID.randomUUID().toString());

                userRepository.save(user);

                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                String accessToken = jwtUtil.generateToken(userDetails);
                String refreshToken = jwtUtil.generateRefreshToken(userDetails);

                return new AuthResponse(accessToken, refreshToken, user.getId(),
                                user.getEmail(), user.getRole().name());
        }

        public AuthResponse login(AuthRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
                var user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                String accessToken = jwtUtil.generateToken(userDetails);
                String refreshToken = jwtUtil.generateRefreshToken(userDetails);

                return new AuthResponse(accessToken, refreshToken, user.getId(),
                                user.getEmail(), user.getRole().name());
        }

        @Transactional
        public AuthResponse loginOAuth2(org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
                if (oauth2User == null) {
                        throw new RuntimeException("OAuth2User is null");
                }
                String email = oauth2User.getAttribute("email");
                if (email == null) {
                        throw new RuntimeException("Email not found from OAuth2 provider");
                }

                Utilisateur utilisateur = (Utilisateur) userRepository.findByEmail(email)
                                .orElseGet(() -> {
                                        Utilisateur newUser = new Utilisateur();
                                        newUser.setEmail(email);
                                        newUser.setNom(
                                                        oauth2User.getAttribute("family_name") != null
                                                                        ? oauth2User.getAttribute("family_name")
                                                                        : "Unknown");
                                        newUser.setPrenom(
                                                        oauth2User.getAttribute("given_name") != null
                                                                        ? oauth2User.getAttribute("given_name")
                                                                        : "Unknown");
                                        newUser.setPassword(passwordEncoder.encode("OAUTH2_USER")); // Dummy password
                                        newUser.setRole(Role.USER);
                                        newUser.setScoreFiabilite(0.0);
                                        newUser.setProfilePublicToken(UUID.randomUUID().toString());
                                        return userRepository.save(newUser);
                                });

                UserDetails userDetails = userDetailsService.loadUserByUsername(utilisateur.getEmail());
                String accessToken = jwtUtil.generateToken(userDetails);
                String refreshToken = jwtUtil.generateRefreshToken(userDetails);

                return new AuthResponse(accessToken, refreshToken, utilisateur.getId(),
                                utilisateur.getEmail(), utilisateur.getRole().name());
        }

        public AuthResponse refreshToken(String refreshToken) {
                String email = jwtUtil.extractUsername(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (!jwtUtil.validateToken(refreshToken, userDetails)) {
                        throw new RuntimeException("Invalid refresh token");
                }

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                String newAccessToken = jwtUtil.generateToken(userDetails);
                String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

                return new AuthResponse(newAccessToken, newRefreshToken, user.getId(), user.getEmail(),
                                user.getRole().name());
        }
}
