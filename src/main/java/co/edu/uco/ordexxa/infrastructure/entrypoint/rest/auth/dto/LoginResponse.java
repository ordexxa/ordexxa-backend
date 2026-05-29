package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.auth.dto;

public record LoginResponse(
        String email,
        String fullName,
        String role,
        String token,
        String message
) {
}
