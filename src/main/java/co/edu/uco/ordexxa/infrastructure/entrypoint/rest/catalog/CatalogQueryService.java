package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.catalog;

import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.DocumentTypeEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.NotificationTemplateEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.SystemParameterEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.DocumentTypeJpaRepository;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.NotificationTemplateJpaRepository;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.SystemParameterJpaRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogQueryService {

    private final DocumentTypeJpaRepository documentTypeRepository;
    private final SystemParameterJpaRepository systemParameterRepository;
    private final NotificationTemplateJpaRepository notificationTemplateRepository;

    public CatalogQueryService(
            final DocumentTypeJpaRepository documentTypeRepository,
            final SystemParameterJpaRepository systemParameterRepository,
            final NotificationTemplateJpaRepository notificationTemplateRepository
    ) {
        this.documentTypeRepository = documentTypeRepository;
        this.systemParameterRepository = systemParameterRepository;
        this.notificationTemplateRepository = notificationTemplateRepository;
    }

    @Cacheable(cacheNames = "catalog:document-types")
    public List<DocumentTypeEntity> getDocumentTypes() {
        return documentTypeRepository.findAll();
    }

    @Cacheable(cacheNames = "catalog:system-parameters")
    public List<SystemParameterEntity> getSystemParameters() {
        return systemParameterRepository.findAll();
    }

    @Cacheable(cacheNames = "catalog:notification-templates")
    public List<NotificationTemplateEntity> getNotificationTemplates() {
        return notificationTemplateRepository.findAll();
    }
}
