package co.edu.uco.ordexxa.initializer;

import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.NotificationTemplateEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.SystemParameterEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.NotificationTemplateJpaRepository;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.SystemParameterJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogInitializer {

    @Bean
    public CommandLineRunner initializeCatalogs(
            final SystemParameterJpaRepository systemParameterRepository,
            final NotificationTemplateJpaRepository notificationTemplateRepository
    ) {
        return args -> {
            createParameterIfNotExists(
                    systemParameterRepository,
                    "AUTH_VERIFICATION_CODE_EXPIRATION_MINUTES",
                    "15",
                    "Tiempo de expiración del código de verificación de cuenta."
            );

            createParameterIfNotExists(
                    systemParameterRepository,
                    "MAX_FAILED_LOGIN_ATTEMPTS",
                    "5",
                    "Número máximo de intentos fallidos antes de bloquear temporalmente la cuenta."
            );

            createParameterIfNotExists(
                    systemParameterRepository,
                    "ACCOUNT_LOCK_MINUTES",
                    "15",
                    "Tiempo de bloqueo temporal de cuenta después de superar intentos fallidos."
            );

            createParameterIfNotExists(
                    systemParameterRepository,
                    "PROVIDER_DEFAULT_STATUS",
                    "ACTIVE",
                    "Estado inicial de un proveedor registrado."
            );

            createTemplateIfNotExists(
                    notificationTemplateRepository,
                    "ACCOUNT_VERIFICATION_CODE",
                    "EMAIL",
                    "Código de verificación Ordexxa",
                    """
                    Hola {{fullName}},

                    Tu código de verificación para Ordexxa es:

                    {{verificationCode}}

                    Este código vence en {{expirationMinutes}} minutos.

                    Ordexxa
                    """
            );

            createTemplateIfNotExists(
                    notificationTemplateRepository,
                    "PROVIDER_CREATED",
                    "SYSTEM",
                    "Proveedor registrado",
                    "El proveedor {{businessName}} fue registrado exitosamente en Ordexxa."
            );
        };
    }

    private void createParameterIfNotExists(
            final SystemParameterJpaRepository repository,
            final String code,
            final String value,
            final String description
    ) {
        if (repository.existsById(code)) {
            return;
        }

        final SystemParameterEntity entity = new SystemParameterEntity();
        entity.setCode(code);
        entity.setValue(value);
        entity.setDescription(description);
        entity.setActive(Boolean.TRUE);

        repository.save(entity);
    }

    private void createTemplateIfNotExists(
            final NotificationTemplateJpaRepository repository,
            final String code,
            final String channel,
            final String subject,
            final String body
    ) {
        if (repository.existsById(code)) {
            return;
        }

        final NotificationTemplateEntity entity = new NotificationTemplateEntity();
        entity.setCode(code);
        entity.setChannel(channel);
        entity.setSubject(subject);
        entity.setBody(body);
        entity.setActive(Boolean.TRUE);

        repository.save(entity);
    }
}
