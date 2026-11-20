package cl.duoc.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API REST - Distribuidora de Gas El Volcán",
                version = "1.0",
                description = "API REST oficial desarrollada con Spring Boot 3/4, Spring Security, JWT y Spring Data JPA para la Distribuidora de Gas El Volcán."
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Ingrese el token JWT obtenido en /api/auth/iniciar-sesion"
)
public class SwaggerConfig {
}
