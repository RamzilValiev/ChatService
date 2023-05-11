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
import ru.iteco.test.model.dto.ChatDto;
import ru.iteco.test.model.dto.ErrorResponseDto;
import ru.iteco.test.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@Tag(name = "Chats", description = "Chat Methods")
public class ChatController {
    private final ChatService chatService;

    @Operation(summary = "Get all chats", description = "Returns a list of all chats")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping()
    public List<ChatDto> getChats() {
        return chatService.findAll();
    }

    @Operation(summary = "Get chat by id (URL)", description = "Returns one chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @GetMapping("/{id}")
    public ChatDto getChat(@PathVariable("id") Long id) {
        return chatService.findById(id);
    }

    @Operation(summary = "Create a new chat", description = "Creates a new chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Chat with the same name already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewChat(@RequestBody @Validated ChatDto chatDto) {
        return chatService.save(chatDto);
    }

    @Operation(summary = "Get chat by id", description = "Returns one chat used together with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @GetMapping("/get")
    public List<ChatDto> getUserChats(@RequestParam("userId") Long userId,
                                      @PageableDefault(
                                              size = Integer.MAX_VALUE,
                                              sort = "created_at",
                                              direction = Sort.Direction.DESC
                                      ) Pageable pageable) {
        return chatService.getUserChats(userId, pageable);
    }
}
