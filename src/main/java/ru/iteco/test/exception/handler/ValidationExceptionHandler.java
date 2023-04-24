package ru.iteco.test.exception.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String handleConstrainsViolationException(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return String.join(",", errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String handleException(MethodArgumentNotValidException e) {
        List<String> errors = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return String.join(", ", errors);
    }
}
