package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.catalog;

import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.DocumentTypeEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.NotificationTemplateEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.SystemParameterEntity;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final MessageSource messageSource;
    private final CatalogQueryService catalogQueryService;

    public CatalogController(
            final MessageSource messageSource,
            final CatalogQueryService catalogQueryService
    ) {
        this.messageSource = messageSource;
        this.catalogQueryService = catalogQueryService;
    }

    @GetMapping("/messages/{code}")
    public ResponseEntity<Map<String, String>> getMessage(
            @PathVariable final String code,
            @RequestParam(defaultValue = "es") final String lang
    ) {
        final Locale locale = Locale.forLanguageTag(lang);

        try {
            final String message = messageSource.getMessage(code, null, locale);

            return ResponseEntity.ok(Map.of(
                    "code", code,
                    "language", lang,
                    "message", message
            ));
        } catch (NoSuchMessageException exception) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", code,
                    "language", lang,
                    "message", "No existe un mensaje registrado para el código indicado."
            ));
        }
    }

    @GetMapping("/document-types")
    public ResponseEntity<List<DocumentTypeEntity>> getDocumentTypes() {
        return ResponseEntity.ok(catalogQueryService.getDocumentTypes());
    }

    @GetMapping("/system-parameters")
    public ResponseEntity<List<SystemParameterEntity>> getSystemParameters() {
        return ResponseEntity.ok(catalogQueryService.getSystemParameters());
    }

    @GetMapping("/notification-templates")
    public ResponseEntity<List<NotificationTemplateEntity>> getNotificationTemplates() {
        return ResponseEntity.ok(catalogQueryService.getNotificationTemplates());
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentCatalogResponse>> getDepartments() {
        return ResponseEntity.ok(catalogQueryService.getDepartments());
    }
}
