package com.aditya.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(max = 200)
    private String bio;

    private String avatarUrl;
}
