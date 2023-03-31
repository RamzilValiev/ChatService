package ru.iteco.test.exception.message;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(Long id) {
        super(String.format("Message with id: %d is not found", id));
    }
}
