package com.reviewplatform.dto;

import java.time.ZonedDateTime;

public record UserResponse(
        Long id,
        String username,
        String email,
        String profileImageUrl,
        String bio,
        ZonedDateTime createdAt
) {}
