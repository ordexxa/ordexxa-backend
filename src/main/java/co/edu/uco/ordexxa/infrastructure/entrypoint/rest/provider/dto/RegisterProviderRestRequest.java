package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.provider.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterProviderRestRequest(

        @NotBlank(message = "{ordexxa.provider.businessName.required}")
        @Size(max = 150, message = "{ordexxa.provider.businessName.max}")
        String businessName,

        @NotBlank(message = "{ordexxa.provider.documentType.required}")
        @Pattern(regexp = "NIT|CC|CE|PASAPORTE", message = "{ordexxa.provider.documentType.invalid}")
        String documentType,

        @NotBlank(message = "{ordexxa.provider.documentNumber.required}")
        @Size(max = 30, message = "{ordexxa.provider.documentNumber.max}")
        String documentNumber,

        @NotBlank(message = "{ordexxa.provider.email.required}")
        @Email(message = "{ordexxa.provider.email.invalid}")
        @Size(max = 120, message = "{ordexxa.provider.email.max}")
        String email,

        @NotBlank(message = "{ordexxa.provider.phoneNumber.required}")
        @Pattern(regexp = "\\d{7,15}", message = "{ordexxa.provider.phoneNumber.invalid}")
        String phoneNumber,

        @NotBlank(message = "{ordexxa.provider.address.required}")
        @Size(max = 200, message = "{ordexxa.provider.address.max}")
        String address
) {
}
