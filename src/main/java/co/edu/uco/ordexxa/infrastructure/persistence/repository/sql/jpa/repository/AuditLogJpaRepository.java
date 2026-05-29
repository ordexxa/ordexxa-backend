package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository;

import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditLogJpaRepository extends JpaRepository<AuditLogEntity, UUID> {
}
