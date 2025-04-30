package com.reviewplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record ContentItemRequest(
        @NotBlank String title,
        String description,
        @NotNull Long categoryId,
        String imageUrl,
        String externalId,
        Map<String, Object> metadata
) {}
