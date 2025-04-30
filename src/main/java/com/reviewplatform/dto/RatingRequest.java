package com.reviewplatform.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RatingRequest(
        @NotNull Long contentItemId,
        Long reviewId,
        @Min(1) @Max(5) Integer value
) {}
