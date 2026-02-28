package com.example.auth.service;

import com.example.auth.dto.*;

public interface AuthService {
    ApiResponse<?> register(RegisterRequest request);
    ApiResponse<AuthResponse> login(LoginRequest request);
    ApiResponse<AuthResponse> refreshToken(RefreshTokenRequest request);
}
