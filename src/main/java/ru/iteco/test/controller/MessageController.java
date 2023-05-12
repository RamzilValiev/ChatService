package ru.iteco.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.iteco.test.model.dto.ChatInfoDto;
import ru.iteco.test.model.dto.ErrorResponseDto;
import ru.iteco.test.model.dto.MessageDto;
import ru.iteco.test.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Message Methods")
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "Get messages", description = "Returns all messages")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping()
    public List<MessageDto> getMessages() {
        return messageService.findAll();
    }

    @Operation(summary = "Get message by id (URL)", description = "Returns one message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Message not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{id}")
    public MessageDto getMessage(@PathVariable("id") Long id) {
        return messageService.findById(id);
    }

    @Operation(summary = "Create new message", description = "Creates a new message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "User and/or chat not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewMessage(@RequestBody @Validated MessageDto messageDto) {
        return messageService.save(messageDto);
    }

    @Operation(summary = "Get all messages in a chat", description = "Returns the messages in the given chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/get")
    public List<MessageDto> getMessages(@RequestParam("chatId") Long chatId,
                                        @PageableDefault(
                                                size = Integer.MAX_VALUE,
                                                sort = "createdAt",
                                                direction = Sort.Direction.DESC
                                        ) Pageable pageable) {
        return messageService.getMessages(chatId, pageable);
    }

    @Operation(summary = "Get chat messages by message fragment", description = "Returns the number of matches for messages in a chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/entry/count")
    public ChatInfoDto getLikeMessages(@RequestParam("chatId") Long chatId,
                                       @RequestParam("text") String text) {
        return messageService.getLikeMessages(chatId, text);
    }
}
