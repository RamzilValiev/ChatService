package ru.iteco.test.model.dto;

import jakarta.validation.constraints.NotEmpty;

public record AuthenticationDto(@NotEmpty(message = "Username should not be empty") String userName,
                                @NotEmpty(message = "Password should not be empty") String password) {
}
