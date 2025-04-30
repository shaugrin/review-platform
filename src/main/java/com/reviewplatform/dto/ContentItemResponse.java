package com.reviewplatform.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

public record ContentItemResponse(
        Long id,
        String title,
        String description,
        Long categoryId,
        String imageUrl,
        String externalId,
        Map<String, Object> metadata,
        BigDecimal avgRating,
        Integer reviewCount,
        ZonedDateTime createdAt
) {}
