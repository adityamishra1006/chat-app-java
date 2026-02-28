package com.example.auth.service;

import com.example.auth.entity.RefreshToken;
import com.example.auth.entity.UserCredentials;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(UserCredentials user);
    Optional<RefreshToken> findByToken(String token);
    RefreshToken verifyExpiration(RefreshToken token);
    void deleteByUser(UserCredentials user);
}
