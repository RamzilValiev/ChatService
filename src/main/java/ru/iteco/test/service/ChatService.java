package ru.iteco.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.iteco.test.exception.ChatAlreadyExistException;
import ru.iteco.test.exception.ChatNotFoundException;
import ru.iteco.test.exception.UserAlreadyExistException;
import ru.iteco.test.model.dto.ChatDto;
import ru.iteco.test.model.dto.ChatErrorResponse;
import ru.iteco.test.model.entity.ChatEntity;
import ru.iteco.test.model.entity.UserEntity;
import ru.iteco.test.repository.ChatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;

    public List<ChatDto> findAll() {
        List<ChatEntity> chatEntities = chatRepository.findAll();
        return chatEntities.stream()
                .map(chatEntity -> convertChatEntityToChatDto(chatEntity))
                .toList();
    }

    private ChatDto convertChatEntityToChatDto(ChatEntity chatEntity) {
        List<Long> usersList = chatEntity.getUsersList()
                .stream()
                .map(UserEntity::getId)
                .toList();
        return new ChatDto(
                chatEntity.getChatName(),
                usersList,
                chatEntity.getCreatedAt()
        );
    }

    public ChatDto findById(Long id) {
        Optional<ChatEntity> chatsByIdOptional = chatRepository.findById(id);
        return chatsByIdOptional.map(chatEntity -> convert(chatEntity))
                .orElseThrow(() -> new ChatNotFoundException(id));
    }

    public ChatDto convert(ChatEntity chatEntity) {
        List<Long> userIds = chatEntity.getUsersList()
                .stream()
                .map(UserEntity::getId)
                .toList();
        return new ChatDto(
                chatEntity.getChatName(),
                userIds,
                chatEntity.getCreatedAt()
        );
    }

    public Long save(ChatDto chatDto) {
        Optional<ChatEntity> chatEntityOptional = chatRepository.findByChatName(chatDto.getChatName());
        if (chatEntityOptional.isPresent()) {
            throw new ChatAlreadyExistException(chatDto.getChatName());
        }

        ChatEntity chatEntity = convertToChatEntity(chatDto);
        chatRepository.save(chatEntity);
        return chatEntity.getId();
    }

    private ChatEntity convertToChatEntity(ChatDto chatDto) {
        ChatEntity chatEntity = new ChatEntity();

        chatEntity.setChatName(chatDto.getChatName());
        List<Long> usersIds = chatDto.getUsersList();

        Optional.ofNullable(usersIds).ifPresent(longs -> {
            List<UserEntity> userEntities = longs.stream()
                    .map(userService::findUserEntityById)
                    .toList();
            chatEntity.setUsersList(userEntities);
        });
        return chatEntity;
    }
}