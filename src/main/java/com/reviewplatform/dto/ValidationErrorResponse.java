package com.reviewplatform.dto;

import java.util.List;

public record ValidationErrorResponse(
        String code,
        String message,
        List<FieldError> errors
) {
    public record FieldError(String field, String message) {}
}