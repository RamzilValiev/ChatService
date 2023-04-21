package ru.iteco.test.exception.authentication;

public class PasswordIncorrectException extends RuntimeException {
    public PasswordIncorrectException(String userName) {
        super(String.format("Incorrect password for a user named: '%s'", userName));
    }
}
