package ru.iteco.test.model.dto;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

public record UserDto(
        @NotEmpty(message = "Username should not be empty ") String userName,
        LocalDateTime createdAt,
        @NotEmpty(message = "Password should not be empty") String password) {
}
