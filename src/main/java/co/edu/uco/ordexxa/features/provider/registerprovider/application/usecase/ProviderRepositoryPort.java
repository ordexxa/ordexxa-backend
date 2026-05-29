package co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase;

import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.domain.ProviderDomain;

public interface ProviderRepositoryPort {

    ProviderDomain save(ProviderDomain provider);

    boolean existsByDocumentNumber(String documentNumber);

    boolean existsByDocumentTypeAndDocumentNumber(String documentType, String documentNumber);

    boolean existsByEmail(String email);
}
