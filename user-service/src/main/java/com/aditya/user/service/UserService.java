package com.aditya.user.service;

import com.aditya.user.dto.UpdateProfileRequest;
import com.aditya.user.dto.UserProfileResponse;

import java.util.UUID;

public interface UserService {
    UserProfileResponse getProfile(UUID userId);
    UserProfileResponse updateProfile(UUID userId, UpdateProfileRequest request);
}
