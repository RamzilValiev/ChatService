package ru.iteco.test.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserErrorResponse {
    private String message;
}
