package com.project.taskflow.service;

import com.project.taskflow.dto.AuthRequest;
import com.project.taskflow.dto.AuthResponse;
import com.project.taskflow.dto.RefreshTokenRequest;
import com.project.taskflow.dto.RegisterRequest;
import com.project.taskflow.entity.User;
import com.project.taskflow.repository.UserRepository;
import com.project.taskflow.security.JwtService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

        @Value("${app.jwt.expiration}")
        private long jwtExpiration;

        @Value("${app.jwt.refresh-expiration}")
        private long refreshExpiration;

        private final UserRepository userRepository;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;

        @Transactional
        public AuthResponse register(RegisterRequest request) {
                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new RuntimeException("Email already exists");
                }

                User user = User.builder()
                                .name(request.getName())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .build();

                userRepository.save(user);

                String token = jwtService.generateAccessToken(user, user.getId(), jwtExpiration);
                String refreshToken = jwtService.generateRefreshToken(user, user.getId(), refreshExpiration);
                return AuthResponse.builder()
                                .token(token)
                                .refreshToken(refreshToken)
                                .build();
        }

        @Transactional(readOnly = true)
        public AuthResponse login(AuthRequest request) {

                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

                if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
                        throw new AccessDeniedException("Invalid Password");
                }

                String token = jwtService.generateAccessToken(user, user.getId(), jwtExpiration);
                String refreshToken = jwtService.generateRefreshToken(user, user.getId(), refreshExpiration);
                return AuthResponse.builder()
                                .token(token)
                                .refreshToken(refreshToken)
                                .build();
        }

        @Transactional(readOnly = true)
        public AuthResponse refreshToken(RefreshTokenRequest request) {
                String refreshToken = request.getRefreshToken();

                // Extract username from refresh token
                UUID userId = jwtService.extractUserId(refreshToken);

                // Load user details
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Validate refresh token
                if (jwtService.isRefreshTokenValidForUser(refreshToken, user)) {
                        throw new RuntimeException("Invalid or expired refresh token");
                }

                // Generate new access token
                String newToken = jwtService.generateAccessToken(user, user.getId(), jwtExpiration);

                return AuthResponse.builder()
                                .token(newToken)
                                .refreshToken(refreshToken)
                                .build();
        }
}