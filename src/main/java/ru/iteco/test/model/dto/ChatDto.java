package ru.iteco.test.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ChatDto(String chatName, List<Long> usersList, LocalDateTime createdAt) {
}
