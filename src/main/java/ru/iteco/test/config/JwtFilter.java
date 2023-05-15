package ru.iteco.test.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import ru.iteco.test.exception.authentication.JwtAuthenticationException;
import ru.iteco.test.model.entity.JwtTokenEntity;
import ru.iteco.test.service.JwtService;
import ru.iteco.test.service.UserEntityDetailsService;

import java.io.IOException;
import java.util.Optional;

public class JwtFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtService jwtService;
    private final UserEntityDetailsService userEntityDetailsService;
    private static final String BEARER = "Bearer ";

    protected JwtFilter(JwtService jwtService,
                        UserEntityDetailsService userEntityDetailsService,
                        AuthenticationManager authenticationManager,
                        JwtSuccessHandler jwtSuccessHandler,
                        JwtFailureHandler jwtFailureHandler) {
        super("/api/**");
        this.jwtService = jwtService;
        this.userEntityDetailsService = userEntityDetailsService;
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationSuccessHandler(jwtSuccessHandler);
        this.setAuthenticationFailureHandler(jwtFailureHandler);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String jwt = Optional.ofNullable(authHeader)
                .filter(header -> !header.isBlank())
                .filter(header -> header.startsWith(BEARER))
                .map(header -> header.substring(BEARER.length()))
                .orElseThrow(() -> new JwtAuthenticationException("Jwt token is empty"));

        if (jwt.isBlank()) {
            throw new JwtAuthenticationException("Invalid JWT token in Bearer header");
        }
        try {
            String userName = jwtService.validateTokenAndRetrieveClaim(jwt);
            JwtTokenEntity jwtTokenEntity = jwtService.findByUsername(userName);

            String token = jwtTokenEntity.getToken();
            String tokenHex = DigestUtils.md5Hex(jwt);

            if (!token.equals(tokenHex)) {
                throw new JwtAuthenticationException("Jwt token missing from database");
            }
            String username = jwtService.validateTokenAndRetrieveClaim(jwt);
            UserDetails userDetails = userEntityDetailsService.loadUserByUsername(username);

            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities());

        } catch (JWTVerificationException e) {
            throw new JwtAuthenticationException("Jwt token is not valid");
        }
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        super.doFilter(request, response, chain);
    }
}
