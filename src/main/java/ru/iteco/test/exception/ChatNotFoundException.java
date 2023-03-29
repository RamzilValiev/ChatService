package ru.iteco.test.exception;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(Long id) {
        super(String.format("Chat with id: %d is not found", id));
    }
}
