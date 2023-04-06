package ru.iteco.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.iteco.test.exception.chat.ChatAlreadyExistException;
import ru.iteco.test.exception.chat.ChatNotFoundException;
import ru.iteco.test.model.dto.ChatDto;
import ru.iteco.test.model.entity.ChatEntity;
import ru.iteco.test.model.entity.UserEntity;
import ru.iteco.test.repository.ChatRepository;

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
                .map(this::mapChatEntityToChatDto)
                .toList();
    }

    public ChatDto findById(Long id) {
        Optional<ChatEntity> chatsByIdOptional = chatRepository.findById(id);
        return chatsByIdOptional.map(this::mapChatEntityToChatDto)
                .orElseThrow(() -> new ChatNotFoundException(id));
    }

    public ChatEntity findChatEntityById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new ChatNotFoundException(id));
    }

    public String save(ChatDto chatDto) {
        Optional<ChatEntity> chatEntityOptional = chatRepository.findByChatName(chatDto.getChatName());
        if (chatEntityOptional.isPresent()) {
            throw new ChatAlreadyExistException(chatDto.getChatName());
        }

        ChatEntity chatEntity = mapChatDtoToChatEntity(chatDto);
        chatRepository.save(chatEntity);
        return String.format("created new chat id: %d", chatEntity.getId());
    }

    public List<ChatDto> getUserChats(Long userId, Pageable pageable) {
        userService.findById(userId);

        return chatRepository.findChatEntitiesByUserId(userId, pageable)
                .stream()
                .map(chatEntity -> new ChatDto(
                        chatEntity.getChatName(),
                        List.of(userId),
                        chatEntity.getCreatedAt()))
                .toList();
    }

    private ChatDto mapChatEntityToChatDto(ChatEntity chatEntity) {
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

    private ChatEntity mapChatDtoToChatEntity(ChatDto chatDto) {
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
