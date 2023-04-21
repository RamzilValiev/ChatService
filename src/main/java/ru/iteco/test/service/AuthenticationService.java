package ru.iteco.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.iteco.test.exception.authentication.PasswordIncorrectException;
import ru.iteco.test.model.dto.AuthenticationDto;
import ru.iteco.test.model.dto.UserDto;
import ru.iteco.test.model.entity.UserEntity;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Map<String, String> registration(UserDto userDto) {
        log.info("A request was received to register a user with the name: {}", userDto.userName());

        UserEntity userEntity = userService.mapToUserEntity(userDto);

        String encodedPassword = bCryptPasswordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userEntity.setRole("ROLE_USER");

        userService.save(userEntity);
        log.info("user named: {} is registered in the database by id: {}", userEntity.getUserName(), userEntity.getId());

        String token = jwtService.generateToken(userDto.userName());
        return Map.of("jwt-token", token);
    }

    public Map<String, String> authorization(AuthenticationDto authenticationDto) {
        Optional<UserEntity> userEntityOptional = userService.findUserByUserName(authenticationDto.userName());

        if (userEntityOptional.isPresent()) {
            String password = userEntityOptional.get().getPassword();
            boolean matches = bCryptPasswordEncoder.matches(authenticationDto.password(), password);

            if (matches) {
                String token = jwtService.generateToken(authenticationDto.userName());
                return Map.of("jwt-token", token);
            } else {
                throw new PasswordIncorrectException(authenticationDto.userName());
            }
        }
        return Collections.emptyMap();
    }
}
