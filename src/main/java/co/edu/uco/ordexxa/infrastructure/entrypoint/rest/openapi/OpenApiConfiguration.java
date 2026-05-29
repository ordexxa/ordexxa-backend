package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI ordexxaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ordexxa API")
                        .description("Documentación OpenAPI del MVP de Ordexxa para autenticación y registro de proveedores.")
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("Ordexxa - Ingeniería de Software 2"))
                        .license(new License()
                                .name("Academic MVP")))
                .externalDocs(new ExternalDocumentation()
                        .description("Arquitectura basada en Clean Architecture, API Gateway y servicios REST."))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("UUID Token")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }
}
