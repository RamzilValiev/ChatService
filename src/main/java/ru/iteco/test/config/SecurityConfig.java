package ru.iteco.test.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.iteco.test.service.JwtService;
import ru.iteco.test.service.UserEntityDetailsService;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserEntityDetailsService userEntityDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtSuccessHandler jwtSuccessHandler;
    private final JwtFailureHandler jwtFailureHandler;
    private static final List<String> REQUEST_MATCHERS = List.of(
            "/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/error"
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll())
                .addFilterBefore(
                        new JwtFilter(
                                jwtService,
                                userEntityDetailsService,
                                authenticationManager(authenticationConfiguration),
                                jwtSuccessHandler,
                                jwtFailureHandler,
                                REQUEST_MATCHERS
                        ),
                        UsernamePasswordAuthenticationFilter.class
                )
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
