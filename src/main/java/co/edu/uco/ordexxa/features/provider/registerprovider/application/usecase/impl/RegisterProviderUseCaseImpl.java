package co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.impl;

import co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport.RegisterProviderInputPort;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport.dto.RegisterProviderRequestDTO;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport.dto.RegisterProviderResponseDTO;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.mapper.RegisterProviderMapper;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.DocumentTypeRepositoryPort;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.MessageCatalogPort;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.ProviderRepositoryPort;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.domain.DocumentTypeRule;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.domain.ProviderDomain;

public class RegisterProviderUseCaseImpl implements RegisterProviderInputPort {

    private final ProviderRepositoryPort providerRepositoryPort;
    private final DocumentTypeRepositoryPort documentTypeRepositoryPort;
    private final MessageCatalogPort messageCatalogPort;

    public RegisterProviderUseCaseImpl(
            final ProviderRepositoryPort providerRepositoryPort,
            final DocumentTypeRepositoryPort documentTypeRepositoryPort,
            final MessageCatalogPort messageCatalogPort
    ) {
        this.providerRepositoryPort = providerRepositoryPort;
        this.documentTypeRepositoryPort = documentTypeRepositoryPort;
        this.messageCatalogPort = messageCatalogPort;
    }

    @Override
    public RegisterProviderResponseDTO execute(final RegisterProviderRequestDTO request) {
        validateRequest(request);
        validateBusinessRules(request);

        final ProviderDomain providerToSave = RegisterProviderMapper.toDomain(request);
        final ProviderDomain savedProvider = providerRepositoryPort.save(providerToSave);

        return RegisterProviderMapper.toResponse(savedProvider);
    }

    private void validateRequest(final RegisterProviderRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException(message("ordexxa.provider.request.required"));
        }

        validateRequiredText(request.businessName(), "ordexxa.provider.businessName.required");
        validateRequiredText(request.documentType(), "ordexxa.provider.documentType.required");
        validateRequiredText(request.documentNumber(), "ordexxa.provider.documentNumber.required");
        validateRequiredText(request.email(), "ordexxa.provider.email.required");
        validateRequiredText(request.phoneNumber(), "ordexxa.provider.phoneNumber.required");
        validateRequiredText(request.address(), "ordexxa.provider.address.required");

        validatePhoneNumber(request.phoneNumber());
        validateDocumentNumber(request.documentType(), request.documentNumber());
    }

    private void validateBusinessRules(final RegisterProviderRequestDTO request) {
        final String documentType = request.documentType().trim().toUpperCase();
        final String documentNumber = request.documentNumber().trim();
        final String email = request.email().trim().toLowerCase();

        if (providerRepositoryPort.existsByDocumentTypeAndDocumentNumber(documentType, documentNumber)) {
            throw new IllegalArgumentException(message("ordexxa.provider.document.duplicated"));
        }

        if (providerRepositoryPort.existsByEmail(email)) {
            throw new IllegalArgumentException(message("ordexxa.provider.email.duplicated"));
        }
    }

    private void validateRequiredText(final String value, final String messageCode) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message(messageCode));
        }
    }

    private void validatePhoneNumber(final String phoneNumber) {
        final String normalizedPhone = phoneNumber.trim();

        if (!normalizedPhone.matches("\\d{7,15}")) {
            throw new IllegalArgumentException(message("ordexxa.provider.phoneNumber.invalid"));
        }
    }

    private String message(final String code, final Object... arguments) {
        return messageCatalogPort.getMessage(code, arguments);
    }

    private void validateDocumentNumber(final String documentTypeValue, final String documentNumberValue) {
        final String documentType = documentTypeValue.trim().toUpperCase();
        final String documentNumber = documentNumberValue.trim();

        final DocumentTypeRule rule = documentTypeRepositoryPort.findByCode(documentType)
                .orElseThrow(() -> new IllegalArgumentException(message("ordexxa.provider.documentType.notFound")));

        if (rule.numericOnly() && !documentNumber.matches("\\d+")) {
            throw new IllegalArgumentException(message("ordexxa.provider.documentNumber.numericOnly", rule.name()));
        }

        if (documentNumber.length() < rule.minLength() || documentNumber.length() > rule.maxLength()) {
            throw new IllegalArgumentException(message(
                    "ordexxa.provider.documentNumber.lengthRange",
                    rule.name(),
                    rule.minLength(),
                    rule.maxLength()
            ));
        }
    }
}
