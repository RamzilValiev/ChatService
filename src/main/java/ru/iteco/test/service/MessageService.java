package ru.iteco.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.iteco.test.exception.message.MessageNotFoundException;
import ru.iteco.test.model.dto.ChatInfoDto;
import ru.iteco.test.model.dto.MessageDto;
import ru.iteco.test.model.entity.ChatEntity;
import ru.iteco.test.model.entity.MessageEntity;
import ru.iteco.test.model.entity.UserEntity;
import ru.iteco.test.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final UserService userService;

    public List<MessageDto> findAll() {
        List<MessageEntity> messageEntities = messageRepository.findAll();
        return messageEntities.stream()
                .map(this::mapMessageEntityToMessageDto)
                .toList();
    }

    public MessageDto findById(Long id) {
        Optional<MessageEntity> messageByIdOptional = messageRepository.findById(id);
        return messageByIdOptional.map(this::mapMessageEntityToMessageDto)
                .orElseThrow(() -> new MessageNotFoundException(id));
    }

    public String save(MessageDto messageDto) {
        log.info(String.format("Received request to save message with text: '%s' in chat: id_%d from user: id_%d",
                messageDto.textMessage(), messageDto.chatId(), messageDto.userId()));

        MessageEntity messageEntity = mapMessageDtoToMessageEntity(messageDto);
        messageRepository.save(messageEntity);

        log.info(String.format("Message with text: '%s' in chat: id_%d from user: id_%d saved in database under id: %d",
                messageDto.textMessage(), messageDto.chatId(), messageDto.userId(), messageEntity.getId()));

        return String.format("created new message id: %d", messageEntity.getId());
    }

    public List<MessageDto> getMessages(Long chatId, Pageable pageable) {
        chatService.findById(chatId);

        return messageRepository.findByChatEntityId(chatId, pageable)
                .stream()
                .map(messageEntity -> new MessageDto(
                        messageEntity.getChatEntity().getId(),
                        messageEntity.getUserEntity().getId(),
                        messageEntity.getTextMessage(),
                        messageEntity.getCreatedAt()))
                .toList();
    }

    public ChatInfoDto getLikeMessages(Long chatId, String text) {
        String chatName = chatService.findById(chatId).chatName();

        Long count = messageRepository.countByChatEntityIdAndTextMessageContainingIgnoreCase(chatId, text);

        return new ChatInfoDto(
                chatName,
                text,
                count
        );
    }

    private MessageEntity mapMessageDtoToMessageEntity(MessageDto messageDto) {
        ChatEntity chatEntity = chatService.findChatEntityById(messageDto.chatId());
        UserEntity userEntity = userService.findUserEntityById(messageDto.userId());

        MessageEntity messageEntity = new MessageEntity();

        messageEntity.setTextMessage(messageDto.textMessage());
        messageEntity.setCreatedAt(messageDto.createdAt());
        messageEntity.setChatEntity(chatEntity);
        messageEntity.setUserEntity(userEntity);

        return messageEntity;
    }

    private MessageDto mapMessageEntityToMessageDto(MessageEntity messageEntity) {
        return new MessageDto(
                messageEntity.getChatEntity().getId(),
                messageEntity.getUserEntity().getId(),
                messageEntity.getTextMessage(),
                messageEntity.getCreatedAt()
        );
    }
}
