package co.edu.uco.ordexxa.features.audit;

import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.AuditLogEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.AuditLogJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuditService {

    private final AuditLogJpaRepository auditLogJpaRepository;

    public AuditService(final AuditLogJpaRepository auditLogJpaRepository) {
        this.auditLogJpaRepository = auditLogJpaRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerSuccess(
            final String eventType,
            final String actorEmail,
            final String actorRole,
            final String resourceType,
            final String resourceId,
            final String description
    ) {
        save(eventType, actorEmail, actorRole, resourceType, resourceId, true, description);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerFailure(
            final String eventType,
            final String actorEmail,
            final String actorRole,
            final String resourceType,
            final String resourceId,
            final String description
    ) {
        save(eventType, actorEmail, actorRole, resourceType, resourceId, false, description);
    }

    private void save(
            final String eventType,
            final String actorEmail,
            final String actorRole,
            final String resourceType,
            final String resourceId,
            final boolean successful,
            final String description
    ) {
        final AuditLogEntity entity = new AuditLogEntity();

        entity.setId(UUID.randomUUID());
        entity.setEventType(eventType);
        entity.setActorEmail(actorEmail);
        entity.setActorRole(actorRole);
        entity.setResourceType(resourceType);
        entity.setResourceId(resourceId);
        entity.setSuccessful(successful);
        entity.setDescription(description);
        entity.setCreatedAt(LocalDateTime.now());

        auditLogJpaRepository.save(entity);
    }
}
