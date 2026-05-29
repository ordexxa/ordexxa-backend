package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository;

import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.ProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProviderJpaRepository extends JpaRepository<ProviderEntity, UUID> {

    boolean existsByDocumentNumber(String documentNumber);

    boolean existsByDocumentTypeAndDocumentNumber(String documentType, String documentNumber);

    boolean existsByEmail(String email);
}
