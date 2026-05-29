package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.provider;

import co.edu.uco.ordexxa.features.audit.AuditService;
import co.edu.uco.ordexxa.features.auth.AuthService;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport.RegisterProviderInputPort;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport.dto.RegisterProviderRequestDTO;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport.dto.RegisterProviderResponseDTO;
import co.edu.uco.ordexxa.infrastructure.entrypoint.rest.provider.dto.RegisterProviderRestRequest;
import co.edu.uco.ordexxa.infrastructure.entrypoint.rest.provider.dto.RegisterProviderRestResponse;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.UserAccountEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proveedores")
public class ProviderController {

    private final RegisterProviderInputPort registerProviderInputPort;
    private final AuthService authService;
    private final AuditService auditService;

    public ProviderController(
            final RegisterProviderInputPort registerProviderInputPort,
            final AuthService authService,
            final AuditService auditService
    ) {
        this.registerProviderInputPort = registerProviderInputPort;
        this.authService = authService;
        this.auditService = auditService;
    }

    @PostMapping
    public ResponseEntity<RegisterProviderRestResponse> registerProvider(
            @RequestHeader(value = "Authorization", required = false) final String authorizationHeader,
            @Valid @RequestBody final RegisterProviderRestRequest request
    ) {
        final UserAccountEntity authenticatedUser = authService.validateAdminToken(authorizationHeader);

        final RegisterProviderRequestDTO useCaseRequest = new RegisterProviderRequestDTO(
                request.businessName(),
                request.documentType(),
                request.documentNumber(),
                request.email(),
                request.phoneNumber(),
                request.address()
        );

        final RegisterProviderResponseDTO useCaseResponse = registerProviderInputPort.execute(useCaseRequest);

        auditService.registerSuccess(
                "PROVIDER_CREATED",
                authenticatedUser.getEmail(),
                authenticatedUser.getRole(),
                "PROVIDER",
                useCaseResponse.id().toString(),
                "Proveedor registrado exitosamente con documento "
                        + useCaseResponse.documentType()
                        + " "
                        + useCaseResponse.documentNumber()
        );

        final RegisterProviderRestResponse response = new RegisterProviderRestResponse(
                useCaseResponse.id(),
                useCaseResponse.businessName(),
                useCaseResponse.documentType(),
                useCaseResponse.documentNumber(),
                useCaseResponse.message()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
