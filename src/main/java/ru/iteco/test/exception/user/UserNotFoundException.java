package ru.iteco.test.exception.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(String.format("User with id: %d is not found", id));
    }
}
