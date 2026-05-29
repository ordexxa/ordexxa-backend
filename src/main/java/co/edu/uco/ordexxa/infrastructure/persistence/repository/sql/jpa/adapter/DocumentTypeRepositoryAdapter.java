package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.adapter;

import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.DocumentTypeRepositoryPort;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.domain.DocumentTypeRule;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.DocumentTypeJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DocumentTypeRepositoryAdapter implements DocumentTypeRepositoryPort {

    private final DocumentTypeJpaRepository documentTypeJpaRepository;

    public DocumentTypeRepositoryAdapter(final DocumentTypeJpaRepository documentTypeJpaRepository) {
        this.documentTypeJpaRepository = documentTypeJpaRepository;
    }

    @Override
    public Optional<DocumentTypeRule> findByCode(final String code) {
        return documentTypeJpaRepository.findById(code)
                .map(entity -> new DocumentTypeRule(
                        entity.getCode(),
                        entity.getName(),
                        entity.getMinLength(),
                        entity.getMaxLength(),
                        entity.getNumericOnly()
                ));
    }
}
