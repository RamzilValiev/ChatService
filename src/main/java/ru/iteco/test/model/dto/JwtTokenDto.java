package ru.iteco.test.model.dto;

import jakarta.validation.constraints.NotEmpty;

public record JwtTokenDto(@NotEmpty(message = "Token is empty") String token) {
}
