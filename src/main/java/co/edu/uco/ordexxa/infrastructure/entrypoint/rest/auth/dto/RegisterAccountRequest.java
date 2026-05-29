package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterAccountRequest(

        @NotBlank(message = "{ordexxa.auth.fullName.required}")
        @Size(max = 120, message = "{ordexxa.auth.fullName.max}")
        String fullName,

        @NotBlank(message = "{ordexxa.auth.email.required}")
        @Email(message = "{ordexxa.auth.email.invalid}")
        @Size(max = 120, message = "{ordexxa.auth.email.max}")
        String email,

        @NotBlank(message = "{ordexxa.auth.password.required}")
        @Size(min = 8, max = 80, message = "{ordexxa.auth.password.size}")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
                message = "{ordexxa.auth.password.strong}"
        )
        String password
) {
}
