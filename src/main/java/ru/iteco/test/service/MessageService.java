package ru.iteco.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.iteco.test.exception.message.MessageNotFoundException;
import ru.iteco.test.model.dto.MessageDto;
import ru.iteco.test.model.entity.ChatEntity;
import ru.iteco.test.model.entity.MessageEntity;
import ru.iteco.test.model.entity.UserEntity;
import ru.iteco.test.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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
        MessageEntity messageEntity = mapMessageDtoToMessageEntity(messageDto);

        messageRepository.save(messageEntity);
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

    private MessageEntity mapMessageDtoToMessageEntity(MessageDto messageDto) {
        ChatEntity chatEntity = chatService.findChatEntityById(messageDto.getChatId());
        UserEntity userEntity = userService.findUserEntityById(messageDto.getUserId());

        MessageEntity messageEntity = new MessageEntity();

        messageEntity.setTextMessage(messageDto.getTextMessage());
        messageEntity.setCreatedAt(messageDto.getCreatedAt());
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
