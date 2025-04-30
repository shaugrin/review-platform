package com.reviewplatform.dto;

public record UserUpdateRequest(
        String bio,
        String profileImageUrl
) {}
