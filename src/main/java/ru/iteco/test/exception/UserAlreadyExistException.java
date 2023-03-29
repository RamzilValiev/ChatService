package ru.iteco.test.exception;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String userName) {
        super(String.format("User with name %s already exists", userName));
    }
}
