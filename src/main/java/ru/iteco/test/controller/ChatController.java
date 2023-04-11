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
import org.springframework.web.bind.annotation.*;
import ru.iteco.test.model.dto.ChatDto;
import ru.iteco.test.model.dto.ErrorResponseDto;
import ru.iteco.test.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
@Tag(name = "Чаты", description = "Методы чата")
public class ChatController {
    private final ChatService chatService;

    @Operation(summary = "Получить все чаты", description = "Возвращает список всех чатов")
    @ApiResponse(responseCode = "200", description = "Операция выполнена, чаты получены")
    @GetMapping()
    public List<ChatDto> getChats() {
        return chatService.findAll();
    }

    @Operation(summary = "Получить чат по id (URL)", description = "Возвращает один чат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена, чат получен"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Чат не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @GetMapping("/{id}")
    public ChatDto getChat(@PathVariable("id") Long id) {
        return chatService.findById(id);
    }

    @Operation(summary = "Создать новый чат", description = "Создает новый чат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Чат успешно создан"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Чат с таким именем уже существует",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewChat(@RequestBody ChatDto chatDto) {
        return chatService.save(chatDto);
    }

    @Operation(summary = "Получить чат по id", description = "Возвращает один чат используется вместе с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Чат не найден",
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
