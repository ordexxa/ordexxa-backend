package co.edu.uco.ordexxa.initializer;

import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.DocumentTypeEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.DocumentTypeJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentTypeInitializer {

    @Bean
    public CommandLineRunner initializeDocumentTypes(final DocumentTypeJpaRepository repository) {
        return args -> {
            createIfNotExists(repository, "NIT", "Número de Identificación Tributaria", 9, 10, true);
            createIfNotExists(repository, "CC", "Cédula de ciudadanía", 6, 10, true);
            createIfNotExists(repository, "CE", "Cédula de extranjería", 6, 12, true);
            createIfNotExists(repository, "PASAPORTE", "Pasaporte", 6, 20, false);
        };
    }

    private void createIfNotExists(
            final DocumentTypeJpaRepository repository,
            final String code,
            final String name,
            final int minLength,
            final int maxLength,
            final boolean numericOnly
    ) {
        if (repository.existsById(code)) {
            return;
        }

        final DocumentTypeEntity entity = new DocumentTypeEntity();
        entity.setCode(code);
        entity.setName(name);
        entity.setMinLength(minLength);
        entity.setMaxLength(maxLength);
        entity.setNumericOnly(numericOnly);

        repository.save(entity);
    }
}
