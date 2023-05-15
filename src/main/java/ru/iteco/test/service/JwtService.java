package ru.iteco.test.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.iteco.test.model.dto.AuthenticationDto;
import ru.iteco.test.model.dto.JwtTokenDto;
import ru.iteco.test.model.entity.JwtTokenEntity;
import ru.iteco.test.model.entity.UserEntity;
import ru.iteco.test.properties.JwtProperties;
import ru.iteco.test.repository.JwtTokenRepository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;
    private final JwtTokenRepository jwtTokenRepository;
    private final UserService userService;

    public String generateToken(String username) {
        Date lifeTime = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("User info")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("Chat_service_App")
                .withExpiresAt(lifeTime)
                .sign(Algorithm.HMAC256(jwtProperties.secret()));
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtProperties.secret()))
                .withSubject("User info")
                .withIssuer("Chat_service_App")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }

    public void save(JwtTokenEntity tokenEntity) {
        jwtTokenRepository.save(tokenEntity);
    }

    public JwtTokenDto update(AuthenticationDto authenticationDto) {
        String newToken = generateToken(authenticationDto.userName());
        String newRefreshToken = generateRefreshToken();

        JwtTokenEntity jwtTokenEntity = findByUsername(authenticationDto.userName());
        jwtTokenEntity.setToken(newToken);
        jwtTokenEntity.setRefreshToken(newRefreshToken);
        save(jwtTokenEntity);

        return new JwtTokenDto(newToken, newRefreshToken);
    }

    public JwtTokenDto update(String username) {
        String newJwtToken = generateToken(username);
        String newRefreshToken = generateRefreshToken();

        JwtTokenEntity jwtTokenEntity = findByUsername(username);
        jwtTokenEntity.setToken(newJwtToken);
        jwtTokenEntity.setRefreshToken(newRefreshToken);
        save(jwtTokenEntity);

        return new JwtTokenDto(newJwtToken, newRefreshToken);
    }

    public JwtTokenEntity mapToEntity(String token, String refreshToken) {
        String username = validateTokenAndRetrieveClaim(token);
        UserEntity userEntity = userService.findUserByUserName(username);

        JwtTokenEntity jwtTokenEntity = new JwtTokenEntity();
        jwtTokenEntity.setToken(token);
        jwtTokenEntity.setRefreshToken(refreshToken);
        jwtTokenEntity.setUserEntity(userEntity);

        return jwtTokenEntity;
    }

    public JwtTokenEntity findByUsername(String username) {
        return jwtTokenRepository.findByUserEntityUserName(username)
                .orElseThrow(() -> new RuntimeException("JwtTokenEntity is not found, invalid username"));
    }
}
