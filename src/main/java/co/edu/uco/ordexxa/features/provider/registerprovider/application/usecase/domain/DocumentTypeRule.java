package co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.domain;

public record DocumentTypeRule(
        String code,
        String name,
        int minLength,
        int maxLength,
        boolean numericOnly
) {
}
