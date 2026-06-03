package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.catalog;

import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.CityEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.DepartmentEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.DocumentTypeEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.NotificationTemplateEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.SystemParameterEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.CityJpaRepository;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.DepartmentJpaRepository;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.DocumentTypeJpaRepository;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.NotificationTemplateJpaRepository;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.SystemParameterJpaRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CatalogQueryService {

    private final DocumentTypeJpaRepository documentTypeRepository;
    private final SystemParameterJpaRepository systemParameterRepository;
    private final NotificationTemplateJpaRepository notificationTemplateRepository;
    private final DepartmentJpaRepository departmentRepository;
    private final CityJpaRepository cityRepository;

    public CatalogQueryService(
            final DocumentTypeJpaRepository documentTypeRepository,
            final SystemParameterJpaRepository systemParameterRepository,
            final NotificationTemplateJpaRepository notificationTemplateRepository,
            final DepartmentJpaRepository departmentRepository,
            final CityJpaRepository cityRepository
    ) {
        this.documentTypeRepository = documentTypeRepository;
        this.systemParameterRepository = systemParameterRepository;
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.departmentRepository = departmentRepository;
        this.cityRepository = cityRepository;
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

    @Cacheable(cacheNames = "catalog:departments")
    public List<DepartmentCatalogResponse> getDepartments() {
        final List<DepartmentEntity> departments = departmentRepository.findByActiveTrueOrderByNameAsc();

        final Map<String, List<CityCatalogResponse>> citiesByDepartment = cityRepository.findActiveCitiesWithActiveDepartment()
                .stream()
                .collect(Collectors.groupingBy(
                        city -> city.getDepartment().getCode(),
                        Collectors.mapping(this::toCityResponse, Collectors.toList())
                ));

        return departments.stream()
                .map(department -> new DepartmentCatalogResponse(
                        department.getCode(),
                        department.getName(),
                        citiesByDepartment.getOrDefault(department.getCode(), List.of())
                ))
                .toList();
    }

    private CityCatalogResponse toCityResponse(final CityEntity city) {
        return new CityCatalogResponse(city.getId(), city.getName());
    }
}
