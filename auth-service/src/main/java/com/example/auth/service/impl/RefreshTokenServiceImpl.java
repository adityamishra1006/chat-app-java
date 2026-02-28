package com.example.auth.service.impl;

import com.example.auth.entity.RefreshToken;
import com.example.auth.entity.UserCredentials;
import com.example.auth.exception.InvalidTokenException;
import com.example.auth.repo.RefreshTokenRepo;
import com.example.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepo refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;


    @Override
    public RefreshToken createRefreshToken(UserCredentials user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().isBefore(LocalDateTime.now())){
            refreshTokenRepository.delete(token);
            throw new InvalidTokenException("Refresh token expired");
        }
        return token;
    }

    @Override
    public void deleteByUser(UserCredentials user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
