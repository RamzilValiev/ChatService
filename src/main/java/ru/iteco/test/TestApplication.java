package ru.iteco.test;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("ru.iteco.test.properties")
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Chat-service project",
                version = "1.0.0",
                description = "chat application",
                termsOfService = "ramz",
                contact = @Contact(
                        name = "Ramzil Valiev",
                        email = "ramzl@bk.ru"
                ),
                license = @License(
                        name = "LICENCE",
                        url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
                )
        )
)
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
