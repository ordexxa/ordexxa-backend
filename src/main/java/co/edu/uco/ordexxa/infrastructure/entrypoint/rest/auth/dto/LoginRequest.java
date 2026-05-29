package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "{ordexxa.auth.email.required}")
        @Email(message = "{ordexxa.auth.email.invalid}")
        String email,

        @NotBlank(message = "{ordexxa.auth.password.required}")
        String password
) {
}
