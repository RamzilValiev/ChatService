package ru.iteco.test.exception;

public class ChatAlreadyExistException extends RuntimeException {

    public ChatAlreadyExistException(String chatName) {
        super(String.format("Chat with name %s already exists", chatName));
    }
}
