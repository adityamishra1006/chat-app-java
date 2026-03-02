package com.aditya.user.mapper;

import com.aditya.user.dto.UserProfileResponse;
import com.aditya.user.entity.User;

public class UserMapper {
    private UserMapper(){

    }

    public static UserProfileResponse toUserProfileResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
