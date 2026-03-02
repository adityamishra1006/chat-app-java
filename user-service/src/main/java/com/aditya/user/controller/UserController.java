package com.aditya.user.controller;

import com.aditya.user.dto.UpdateProfileRequest;
import com.aditya.user.dto.UserProfileResponse;
import com.aditya.user.service.UserService;
import com.aditya.user.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public UserProfileResponse getProfile(
            @RequestHeader(AppConstants.HEADER_USER_ID) UUID userId
    ) {
        log.info("UserId received from gateway: {}", userId);
        return userService.getProfile(userId);
    }

    @PutMapping("/profile")
    public UserProfileResponse updateProfile(
            @RequestHeader(AppConstants.HEADER_USER_ID) UUID userId,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return userService.updateProfile(userId, request);
    }
}