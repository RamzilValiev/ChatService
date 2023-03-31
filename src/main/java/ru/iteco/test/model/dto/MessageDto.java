package ru.iteco.test.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class MessageDto {

    private Long chatId;
    private Long userId;
    private String textMessage;
    private LocalDateTime createdAt;
}
