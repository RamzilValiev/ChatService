package ru.iteco.test.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.iteco.test.exception.user.UserAlreadyExistException;
import ru.iteco.test.exception.user.UserNotFoundException;
import ru.iteco.test.model.dto.ErrorResponseDto;

import java.time.LocalDateTime;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleException(UserNotFoundException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleException(UserAlreadyExistException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
