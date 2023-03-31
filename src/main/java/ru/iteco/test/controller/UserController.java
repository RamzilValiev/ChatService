package ru.iteco.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.iteco.test.model.dto.UserDto;
import ru.iteco.test.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor //TODO delete, создает конструктор для полей требующих инициализации
public class UserController {
    private final UserService userService;

    @GetMapping()
    public List<UserDto> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewUser(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }
}
