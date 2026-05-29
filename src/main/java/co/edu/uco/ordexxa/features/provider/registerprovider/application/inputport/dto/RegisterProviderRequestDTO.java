package co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport.dto;

public record RegisterProviderRequestDTO(
        String businessName,
        String documentType,
        String documentNumber,
        String email,
        String phoneNumber,
        String address
) {
}
