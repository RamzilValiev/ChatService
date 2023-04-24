package ru.iteco.test.exception.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.iteco.test.exception.authentication.PasswordIncorrectException;
import ru.iteco.test.exception.authentication.UserNotFoundByNameException;
import ru.iteco.test.model.dto.ErrorResponseDto;

import java.time.LocalDateTime;

@Log4j2
@RestControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ErrorResponseDto handleException(PasswordIncorrectException e) {
        log.error(e.getMessage());
        return new ErrorResponseDto(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ErrorResponseDto handleException(UserNotFoundByNameException e) {
        log.error(e.getMessage());
        return new ErrorResponseDto(e.getMessage(), LocalDateTime.now());
    }
}
