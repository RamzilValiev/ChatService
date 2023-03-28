package ru.iteco.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.iteco.test.model.dto.UserErrorResponse;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse("User with this id is not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserAlreadyExistException e) {
        UserErrorResponse response = new UserErrorResponse("User with the same name already exists");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
