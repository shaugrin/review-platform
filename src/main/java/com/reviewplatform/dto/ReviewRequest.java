package com.reviewplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewRequest(
        @NotNull Long contentItemId,
        @NotBlank String title,
        @NotBlank String content
) {}
