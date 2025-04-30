package com.reviewplatform.dto;

import java.time.ZonedDateTime;

public record RatingResponse(
        Long id,
        Long userId,
        Long contentItemId,
        Long reviewId,
        Integer value,
        ZonedDateTime createdAt
) {}
