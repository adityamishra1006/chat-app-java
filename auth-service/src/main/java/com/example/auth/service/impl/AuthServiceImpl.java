package com.example.auth.service.impl;

import com.example.auth.common.ErrorCode;
import com.example.auth.dto.*;
import com.example.auth.entity.RefreshToken;
import com.example.auth.entity.UserCredentials;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.repo.RefreshTokenRepo;
import com.example.auth.repo.UserCredentialsRepo;
import com.example.auth.security.JwtUtils;
import com.example.auth.security.UserPrincipal;
import com.example.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialsRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Value("${jwt.access-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    @Override
    public ApiResponse<?> register(RegisterRequest request) {
        if(userRepo.existsByUsername(request.getUsername())){
            return ApiResponse.builder()
                    .success(false)
                    .message("Username already exists")
                    .errorCode(ErrorCode.USER_ALREADY_EXISTS)
                    .timeStamp(LocalDateTime.now())
                    .build();
        }

        if(userRepo.existsByEmail(request.getEmail())){
            return ApiResponse.builder()
                    .success(false)
                    .message("Email Already exists")
                    .errorCode(ErrorCode.USER_ALREADY_EXISTS)
                    .timeStamp(LocalDateTime.now())
                    .build();
        }

        UserCredentials user = UserCredentials.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .isActive(true)
                .build();

        userRepo.save(user);

        return ApiResponse.builder()
                .success(true)
                .message("User registered successfully")
                .timeStamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(userPrincipal);
        RefreshToken refreshToken = createRefreshToken(userPrincipal.getId());

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration/1000)
                .build();

        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login Successful")
                .data(authResponse)
                .timeStamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<AuthResponse> refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));

        if(refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        UserCredentials user = refreshToken.getUser();
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String newAccessToken = jwtUtils.generateAccessToken(userPrincipal);

        AuthResponse response = AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration/1000)
                .build();

        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(response)
                .timeStamp(LocalDateTime.now())
                .build();
    }

    private RefreshToken createRefreshToken(UUID userId){
        UserCredentials user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

}
