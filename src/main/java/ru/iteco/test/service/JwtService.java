package ru.iteco.test.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.iteco.test.properties.JwtProperties;

import java.time.ZonedDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

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

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtProperties.secret()))
                .withSubject("User info")
                .withIssuer("Chat_service_App")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
