package ru.iteco.test.model.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(String message, LocalDateTime timestamp) {
}
