package com.reviewplatform.dto;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        String icon
) {}
