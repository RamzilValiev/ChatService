package ru.iteco.test.exception.authentication;

/**
 * Used only within authorization
 */
public class UserNotFoundByNameException extends RuntimeException {
    public UserNotFoundByNameException(String userName) {
        super(String.format("User with name: %s is not found", userName));
    }
}
