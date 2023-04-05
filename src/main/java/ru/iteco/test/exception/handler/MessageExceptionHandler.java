package ru.iteco.test.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.iteco.test.exception.message.MessageNotFoundException;
import ru.iteco.test.exception.user.UserNotFoundException;
import ru.iteco.test.model.response.MessageErrorResponse;

@ControllerAdvice
public class MessageExceptionHandler {
    @ExceptionHandler
    private ResponseEntity<MessageErrorResponse> handleException(MessageNotFoundException e) {
        MessageErrorResponse messageErrorResponse = new MessageErrorResponse(e.getMessage());
        return new ResponseEntity<>(messageErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<MessageErrorResponse> handleException(UserNotFoundException e) {
        MessageErrorResponse messageErrorResponse = new MessageErrorResponse(e.getMessage());
        return new ResponseEntity<>(messageErrorResponse, HttpStatus.NOT_FOUND);
    }
}
