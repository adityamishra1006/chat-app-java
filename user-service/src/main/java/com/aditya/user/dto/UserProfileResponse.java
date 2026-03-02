package com.aditya.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserProfileResponse {
    private UUID id;
    private String username;
    private String email;
    private String bio;
    private String avatarUrl;
}
