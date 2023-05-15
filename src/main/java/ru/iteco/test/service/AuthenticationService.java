package ru.iteco.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.iteco.test.exception.authentication.PasswordIncorrectException;
import ru.iteco.test.model.dto.AuthenticationDto;
import ru.iteco.test.model.dto.JwtTokenDto;
import ru.iteco.test.model.dto.UserDto;
import ru.iteco.test.model.entity.JwtTokenEntity;
import ru.iteco.test.model.entity.UserEntity;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JwtTokenDto register(UserDto userDto) {
        log.info("A request was received to register a user with the name: {}", userDto.userName());
        UserEntity userEntity = userService.mapToUserEntity(userDto);

        String encodedPassword = bCryptPasswordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userEntity.setRole("ROLE_USER");

        userService.save(userEntity);
        log.info("user named: {} is registered in the database by id: {}", userEntity.getUserName(), userEntity.getId());

        String token = jwtService.generateToken(userDto.userName());
        String refreshToken = jwtService.generateRefreshToken();

        JwtTokenEntity jwtTokenEntity = jwtService.mapToEntity(token, refreshToken);
        jwtService.save(jwtTokenEntity);

        log.info("Token and Refresh Token for user named: {} saved to database", userEntity.getUserName());
        return new JwtTokenDto(token, refreshToken);
    }

    public JwtTokenDto authorize(AuthenticationDto authenticationDto) {
        UserEntity userEntity = userService.findUserByUserName(authenticationDto.userName());

        log.info("Token and Refresh Token have been updated for user named: {}", authenticationDto.userName());

        String password = userEntity.getPassword();
        boolean matches = bCryptPasswordEncoder.matches(authenticationDto.password(), password);

        if (!matches) {
            throw new PasswordIncorrectException(authenticationDto.userName());
        }
        return jwtService.update(authenticationDto);
    }

    public JwtTokenDto refreshTokens(JwtTokenDto jwtTokenDto) {
        String username = jwtService.validateTokenAndRetrieveClaim(jwtTokenDto.token());
        JwtTokenEntity jwtTokenEntity = jwtService.findByUsername(username);

        String refreshTokenFromBase = jwtTokenEntity.getRefreshToken();
        String refreshTokenToHex = DigestUtils.md5Hex(jwtTokenDto.refreshToken());

        if (!refreshTokenToHex.equals(refreshTokenFromBase)) {
            throw new RuntimeException("Refresh Tokens do not match");
        }
        log.info("Token and Refresh Token have been updated for user named: {}", username);
        return jwtService.update(username);
    }
}
