package ru.iteco.test.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ChatDto {

    private String chatName;
    private List<Long> usersList;
    private LocalDateTime createdAt;
}
