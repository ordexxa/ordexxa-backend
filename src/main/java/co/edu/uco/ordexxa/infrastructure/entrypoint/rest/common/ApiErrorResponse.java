package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.common;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String code,
        String path,
        List<String> messages
) {
}
