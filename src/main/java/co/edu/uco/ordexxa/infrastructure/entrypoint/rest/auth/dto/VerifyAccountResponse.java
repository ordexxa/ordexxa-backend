package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.auth.dto;

public record VerifyAccountResponse(
        String email,
        String message
) {
}
