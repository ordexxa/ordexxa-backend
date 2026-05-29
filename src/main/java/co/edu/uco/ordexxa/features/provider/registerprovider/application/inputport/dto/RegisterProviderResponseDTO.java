package co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport.dto;

import java.util.UUID;

public record RegisterProviderResponseDTO(
        UUID id,
        String businessName,
        String documentType,
        String documentNumber,
        String message
) {
}
