package ru.iteco.test.model.dto;

import jakarta.validation.constraints.NotEmpty;

public record JwtTokenDto(@NotEmpty(message = "Token should not be empty") String token,
                          @NotEmpty(message = "Refresh Token should not be empty")String refreshToken) {
}
