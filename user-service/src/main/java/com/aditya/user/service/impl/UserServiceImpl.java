package com.aditya.user.service.impl;

import com.aditya.user.dto.UpdateProfileRequest;
import com.aditya.user.dto.UserProfileResponse;
import com.aditya.user.entity.User;
import com.aditya.user.exception.ResourceNotFoundException;
import com.aditya.user.mapper.UserMapper;
import com.aditya.user.repo.UserRepo;
import com.aditya.user.service.UserService;
import com.aditya.user.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public UserProfileResponse getProfile(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));

        return UserMapper.toUserProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));

        user.setBio(request.getBio());
        user.setAvatarUrl(request.getAvatarUrl());

        User updateUser = userRepo.save(user);

        return UserMapper.toUserProfileResponse(updateUser);
    }
}
