package co.edu.uco.ordexxa.features.provider.registerprovider.application.mapper;

import co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport.dto.RegisterProviderRequestDTO;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport.dto.RegisterProviderResponseDTO;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.domain.ProviderDomain;

public final class RegisterProviderMapper {

    private RegisterProviderMapper() {
    }

    public static ProviderDomain toDomain(final RegisterProviderRequestDTO request) {
        return ProviderDomain.create(
                request.businessName().trim(),
                request.documentType().trim().toUpperCase(),
                request.documentNumber().trim(),
                request.email().trim().toLowerCase(),
                request.phoneNumber().trim(),
                request.address().trim()
        );
    }

    public static RegisterProviderResponseDTO toResponse(final ProviderDomain provider) {
        return new RegisterProviderResponseDTO(
                provider.getId(),
                provider.getBusinessName(),
                provider.getDocumentType(),
                provider.getDocumentNumber(),
                "Proveedor registrado exitosamente"
        );
    }
}
