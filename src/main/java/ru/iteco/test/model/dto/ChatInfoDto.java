package ru.iteco.test.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChatInfoDto {

    private String chatName;
    private String text;
    private Long found;
}
