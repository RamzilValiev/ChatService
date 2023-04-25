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
import ru.iteco.test.model.dto.AuthenticationDto;
import ru.iteco.test.model.dto.ErrorResponseDto;
import ru.iteco.test.model.dto.JwtTokenDto;
import ru.iteco.test.model.dto.UserDto;
import ru.iteco.test.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Registration and Authorization")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "User registration", description = "Saves a new user to the database")
    @ApiResponse(responseCode = "201", description = "Success")
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtTokenDto registration(@RequestBody @Validated UserDto userDto) {
        return authenticationService.registration(userDto);
    }

    @Operation(summary = "User authorization", description = "Compares the data of the user who is being authorized with the data of the user in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Incorrect password",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PostMapping("/login")
    public JwtTokenDto authorization(@RequestBody @Validated AuthenticationDto authenticationDto) {
        return authenticationService.authorization(authenticationDto);
    }
}
