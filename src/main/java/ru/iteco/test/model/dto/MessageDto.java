package ru.iteco.test.model.dto;

import java.time.LocalDateTime;

public record MessageDto(Long chatId, Long userId, String textMessage, LocalDateTime createdAt) {
}
