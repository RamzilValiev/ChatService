package ru.iteco.test.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.iteco.test.model.dto.ChatDto;
import ru.iteco.test.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping()
    public List<ChatDto> getChats() {
        return chatService.findAll();
    }

    @GetMapping("/{id}")
    public ChatDto getChat(@PathVariable ("id") Long id) {
        return chatService.findById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createNewChat(@RequestBody ChatDto chatDto) {
        return chatService.save(chatDto);
    }

}
