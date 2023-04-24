package ru.iteco.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.iteco.test.exception.authentication.PasswordIncorrectException;
import ru.iteco.test.model.dto.AuthenticationDto;
import ru.iteco.test.model.dto.JwtTokenDto;
import ru.iteco.test.model.dto.UserDto;
import ru.iteco.test.model.entity.UserEntity;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JwtTokenDto registration(UserDto userDto) {
        log.info("A request was received to register a user with the name: {}", userDto.userName());

        UserEntity userEntity = userService.mapToUserEntity(userDto);

        String encodedPassword = bCryptPasswordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userEntity.setRole("ROLE_USER");

        userService.save(userEntity);
        log.info("user named: {} is registered in the database by id: {}", userEntity.getUserName(), userEntity.getId());

        String token = jwtService.generateToken(userDto.userName());
        return new JwtTokenDto(token);
    }

    public JwtTokenDto authorization(AuthenticationDto authenticationDto) {
        UserEntity userEntity = userService.findUserByUserName(authenticationDto.userName());

        return Optional.of(userEntity)
                .map(UserEntity::getPassword)
                .map(password -> bCryptPasswordEncoder.matches(authenticationDto.password(), password))
                .filter(Boolean::booleanValue)
                .map(m -> jwtService.generateToken(authenticationDto.userName()))
                .map(JwtTokenDto::new)
                .orElseThrow(() -> new PasswordIncorrectException(authenticationDto.userName()));
    }
}
