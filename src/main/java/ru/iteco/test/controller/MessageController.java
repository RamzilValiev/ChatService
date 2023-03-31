package ru.iteco.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.iteco.test.model.dto.MessageDto;
import ru.iteco.test.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping()
    public List<MessageDto> getMessages() {
        return messageService.findAll();
    }

    @GetMapping("/{id}")
    public MessageDto getMessage(@PathVariable("id") Long id) {
        return messageService.findById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewMessage(@RequestBody MessageDto messageDto) {
        return messageService.save(messageDto);
    }
}
