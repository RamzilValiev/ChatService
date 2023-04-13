package ru.iteco.test.model.dto;

import java.time.LocalDateTime;

public record UserDto(String userName, LocalDateTime createdAt) {
}
