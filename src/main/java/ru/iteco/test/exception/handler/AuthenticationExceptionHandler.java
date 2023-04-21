package ru.iteco.test.exception.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.iteco.test.exception.authentication.PasswordIncorrectException;
import ru.iteco.test.exception.authentication.UserNotFoundByNameException;
import ru.iteco.test.model.dto.ErrorResponseDto;

import java.time.LocalDateTime;

@Log4j2
@ControllerAdvice
public class AuthenticationExceptionHandler {
    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleException(PasswordIncorrectException e) {
        log.error(e.getMessage());

        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleException(UserNotFoundByNameException e) {
        log.error(e.getMessage());

        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
