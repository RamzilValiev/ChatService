package ru.iteco.test.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private String userName;
    private LocalDateTime createdAt;
}
