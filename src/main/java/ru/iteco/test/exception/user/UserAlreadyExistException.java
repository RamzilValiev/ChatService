package ru.iteco.test.exception.user;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String userName) {
        super(String.format("User with name '%s' already exists", userName));
    }
}
