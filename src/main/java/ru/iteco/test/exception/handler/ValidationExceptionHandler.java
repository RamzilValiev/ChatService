package ru.iteco.test.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstrainsViolationException(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getMessage())
                .toList();
        return ResponseEntity.badRequest().body(String.join(",", errors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleException(MethodArgumentNotValidException e) {
        List<String> errors = e.getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest().body(String.join(", ", errors));
    }
}
