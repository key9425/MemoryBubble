package com.ssafy.memorybubble.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(servers = {
        @Server(url = "http://localhost:8080", description = "로컬 서버"),
        @Server(url = "https://memorybubble.site", description = "개발 서버")
})
@Configuration
public class SwaggerConfig {
    // Swagger UI: http://localhost:8080/swagger-ui/index.html

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .info(info());
    }

    private Info info() {
        return new Info()
                .title("추억방울 API")
                .description("추억방울 API 문서입니다")
                .version("1.0");
    }
}
