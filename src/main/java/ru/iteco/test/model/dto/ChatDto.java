package ru.iteco.test.model.dto;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

public record ChatDto(
        @NotEmpty(message = "Chat name should not ne empty") String chatName,
        @NotEmpty(message = "usersList should not ne empty") List<Long> usersList,
        LocalDateTime createdAt) {
}
