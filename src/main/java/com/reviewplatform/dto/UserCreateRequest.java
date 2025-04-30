package com.reviewplatform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank String username,
        @Email @NotBlank String email,
        @Size(min = 8) String password
) {}