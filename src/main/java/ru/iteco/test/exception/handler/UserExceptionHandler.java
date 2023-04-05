package ru.iteco.test.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.iteco.test.exception.user.UserAlreadyExistException;
import ru.iteco.test.exception.user.UserNotFoundException;
import ru.iteco.test.model.response.UserErrorResponse;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserAlreadyExistException e) {
        UserErrorResponse response = new UserErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
