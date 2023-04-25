package ru.iteco.test.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.iteco.test.exception.user.UserAlreadyExistException;
import ru.iteco.test.exception.user.UserNotFoundException;
import ru.iteco.test.model.dto.ErrorResponseDto;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponseDto handleException(UserNotFoundException e) {
        log.error(e.getMessage());
        return new ErrorResponseDto(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    private ErrorResponseDto handleException(UserAlreadyExistException e) {
        log.error(e.getMessage());
        return new ErrorResponseDto(e.getMessage(), LocalDateTime.now());
    }
}
