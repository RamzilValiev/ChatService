package ru.iteco.test.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MessageDto(
       @NotNull(message = "chatId should not be empty") Long chatId,
       @NotNull(message = "userId should not be empty") Long userId,
       @NotEmpty(message = "message should not be empty") String textMessage,
        LocalDateTime createdAt) {
}
