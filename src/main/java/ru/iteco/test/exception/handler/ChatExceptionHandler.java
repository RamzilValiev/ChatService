package ru.iteco.test.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.iteco.test.exception.chat.ChatAlreadyExistException;
import ru.iteco.test.exception.chat.ChatNotFoundException;
import ru.iteco.test.model.dto.ErrorResponseDto;

import java.time.LocalDateTime;

@ControllerAdvice
public class ChatExceptionHandler {
    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleException(ChatNotFoundException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleException(ChatAlreadyExistException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
