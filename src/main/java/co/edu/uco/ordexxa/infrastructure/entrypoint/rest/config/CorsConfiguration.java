package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfiguration {

    private final String[] allowedOrigins;

    public CorsConfiguration(
            @Value("${ordexxa.cors.allowed-origins:http://localhost:8080,http://localhost:5173,http://localhost:4173}")
            final String allowedOrigins
    ) {
        this.allowedOrigins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isBlank())
                .toArray(String[]::new);
    }

    @Bean
    public WebMvcConfigurer ordexxaCorsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods(
                                "GET",
                                "POST",
                                "PUT",
                                "PATCH",
                                "DELETE",
                                "OPTIONS"
                        )
                        .allowedHeaders("*")
                        .exposedHeaders(
                                "X-Ordexxa-WAF",
                                "X-Content-Type-Options",
                                "X-Frame-Options",
                                "Content-Security-Policy",
                                "Referrer-Policy"
                        )
                        .maxAge(3600);
            }
        };
    }
}
