package co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase;

import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.domain.DocumentTypeRule;

import java.util.Optional;

public interface DocumentTypeRepositoryPort {

    Optional<DocumentTypeRule> findByCode(String code);
}
