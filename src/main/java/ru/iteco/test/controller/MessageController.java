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
import ru.iteco.test.model.dto.ChatInfoDto;
import ru.iteco.test.model.dto.ErrorResponseDto;
import ru.iteco.test.model.dto.MessageDto;
import ru.iteco.test.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Tag(name = "Сообщения", description = "Методы сообщений")
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "Получить сообщения", description = "Возвращает все сообщения")
    @ApiResponse(responseCode = "200", description = "Операция выполнена")
    @GetMapping()
    public List<MessageDto> getMessages() {
        return messageService.findAll();
    }

    @Operation(summary = "Получить сообщение по id (URL)", description = "Возвращает одно сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сообщение не найдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{id}")
    public MessageDto getMessage(@PathVariable("id") Long id) {
        return messageService.findById(id);
    }

    @Operation(summary = "Создать новое сообщение", description = "Создает новое сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Операция выполнена"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь и/или чат не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewMessage(@RequestBody MessageDto messageDto) {
        return messageService.save(messageDto);
    }

    @Operation(summary = "Получить все сообщения в чате", description = "Возвращает сообщения в данном чате")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Чат не найден",
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

    @Operation(summary = "Получить сообщения в чате по фрагменту сообщения", description = "Возвращает количество совпадений по сообщениям в чате")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Чат не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/entry/count")
    public ChatInfoDto getLikeMessages(@RequestParam("chatId") Long chatId,
                                       @RequestParam("text") String text) {
        return messageService.getLikeMessages(chatId, text);
    }
}
