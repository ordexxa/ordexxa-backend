package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.provider.dto;

import java.util.UUID;

public record RegisterProviderRestResponse(
        UUID id,
        String businessName,
        String documentType,
        String documentNumber,
        String message
) {
}
