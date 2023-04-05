package ru.iteco.test.exception.chat;

public class ChatAlreadyExistException extends RuntimeException {
    public ChatAlreadyExistException(String chatName) {
        super(String.format("Chat with name '%s' already exists", chatName));
    }
}
