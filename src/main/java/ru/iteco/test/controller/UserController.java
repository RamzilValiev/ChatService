package ru.iteco.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.iteco.test.model.dto.ErrorResponseDto;
import ru.iteco.test.model.dto.UserDto;
import ru.iteco.test.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User Methods")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get all users", description = "Returns all users")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping()
    public List<UserDto> getUsers() {
        return userService.findAll();
    }

    @Operation(summary = "Get user by id", description = "Returns one user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "User is not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @Operation(summary = "Create a new user", description = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(
                    responseCode = "409",
                    description = "User with the same name already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewUser(@RequestBody @Validated UserDto userDto) {
        return userService.save(userDto);
    }
}
