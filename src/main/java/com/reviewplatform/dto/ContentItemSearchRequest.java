package com.reviewplatform.dto;

import java.util.Map;

public record ContentItemSearchRequest(
        String query,
        Long categoryId,
        String sortBy,
        Map<String, Object> metadataFilter
) {}
