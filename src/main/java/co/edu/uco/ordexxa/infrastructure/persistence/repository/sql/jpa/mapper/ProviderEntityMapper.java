package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.mapper;

import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.domain.ProviderDomain;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.ProviderEntity;

public final class ProviderEntityMapper {

    private ProviderEntityMapper() {
    }

    public static ProviderEntity toEntity(final ProviderDomain domain) {
        final ProviderEntity entity = new ProviderEntity();

        entity.setId(domain.getId());
        entity.setBusinessName(domain.getBusinessName());
        entity.setDocumentType(domain.getDocumentType());
        entity.setDocumentNumber(domain.getDocumentNumber());
        entity.setEmail(domain.getEmail());
        entity.setPhoneNumber(domain.getPhoneNumber());
        entity.setAddress(domain.getAddress());
        entity.setActive(domain.getActive());
        entity.setCreatedAt(domain.getCreatedAt());

        return entity;
    }

    public static ProviderDomain toDomain(final ProviderEntity entity) {
        return new ProviderDomain(
                entity.getId(),
                entity.getBusinessName(),
                entity.getDocumentType(),
                entity.getDocumentNumber(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getAddress(),
                entity.getActive(),
                entity.getCreatedAt()
        );
    }
}
