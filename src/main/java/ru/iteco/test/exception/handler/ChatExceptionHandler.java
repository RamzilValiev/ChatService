package ru.iteco.test.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.iteco.test.exception.chat.ChatAlreadyExistException;
import ru.iteco.test.exception.chat.ChatNotFoundException;
import ru.iteco.test.model.dto.ChatErrorResponse;

@ControllerAdvice
public class ChatExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ChatErrorResponse> handleException(ChatNotFoundException e) {
        ChatErrorResponse chatErrorResponse = new ChatErrorResponse(e.getMessage());
        return new ResponseEntity<>(chatErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ChatErrorResponse> handleException(ChatAlreadyExistException e) {
        ChatErrorResponse response = new ChatErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
