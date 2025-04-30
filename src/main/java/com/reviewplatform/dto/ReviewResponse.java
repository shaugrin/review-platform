package com.reviewplatform.dto;

import java.time.ZonedDateTime;

public record ReviewResponse(
        Long id,
        Long userId,
        Long contentItemId,
        String title,
        String content,
        Integer likeCount,
        ZonedDateTime createdAt
) {}
