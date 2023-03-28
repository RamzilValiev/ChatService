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

    // ----------------------------   Help method ----------------------------

    @GetMapping()
    public List<UserDto> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Integer id) {
        return userService.findById(id);
    }

    // ----------------------------   Help method ----------------------------

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createNewUser(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

}
