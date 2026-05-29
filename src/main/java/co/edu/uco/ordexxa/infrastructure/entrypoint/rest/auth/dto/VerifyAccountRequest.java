package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerifyAccountRequest(

        @NotBlank(message = "{ordexxa.auth.email.required}")
        @Email(message = "{ordexxa.auth.email.invalid}")
        String email,

        @NotBlank(message = "{ordexxa.auth.verificationCode.required}")
        @Size(min = 6, max = 6, message = "{ordexxa.auth.verificationCode.size}")
        String code
) {
}
