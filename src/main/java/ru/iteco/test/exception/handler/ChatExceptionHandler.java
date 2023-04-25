package ru.iteco.test.exception.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.iteco.test.exception.chat.ChatAlreadyExistException;
import ru.iteco.test.exception.chat.ChatNotFoundException;
import ru.iteco.test.model.dto.ErrorResponseDto;

import java.time.LocalDateTime;

@Log4j2
@RestControllerAdvice
public class ChatExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponseDto handleException(ChatNotFoundException e) {
        log.error(e.getMessage());
        return new ErrorResponseDto(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    private ErrorResponseDto handleException(ChatAlreadyExistException e) {
        log.error(e.getMessage());
        return new ErrorResponseDto(e.getMessage(), LocalDateTime.now());
    }
}
