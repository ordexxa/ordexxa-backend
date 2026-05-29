package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.adapter;

import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.ProviderRepositoryPort;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.domain.ProviderDomain;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.ProviderEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.mapper.ProviderEntityMapper;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.ProviderJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProviderRepositoryAdapter implements ProviderRepositoryPort {

    private final ProviderJpaRepository providerJpaRepository;

    public ProviderRepositoryAdapter(final ProviderJpaRepository providerJpaRepository) {
        this.providerJpaRepository = providerJpaRepository;
    }

    @Override
    public ProviderDomain save(final ProviderDomain provider) {
        final ProviderEntity entityToSave = ProviderEntityMapper.toEntity(provider);
        final ProviderEntity savedEntity = providerJpaRepository.save(entityToSave);

        return ProviderEntityMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByDocumentNumber(final String documentNumber) {
        return providerJpaRepository.existsByDocumentNumber(documentNumber);
    }

    @Override
    public boolean existsByDocumentTypeAndDocumentNumber(final String documentType, final String documentNumber) {
        return providerJpaRepository.existsByDocumentTypeAndDocumentNumber(documentType, documentNumber);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return providerJpaRepository.existsByEmail(email);
    }
}
