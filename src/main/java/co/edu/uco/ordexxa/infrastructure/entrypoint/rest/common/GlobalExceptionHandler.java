package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String CODE_UNAUTHORIZED = "ORD-SEC-001";
    private static final String CODE_BAD_REQUEST = "ORD-REQ-001";
    private static final String CODE_VALIDATION_ERROR = "ORD-VAL-001";
    private static final String CODE_INTERNAL_ERROR = "ORD-SRV-001";
    private static final String CODE_UNEXPECTED_ERROR = "ORD-SRV-999";

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiErrorResponse> handleSecurityException(
            final SecurityException exception,
            final HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized",
                CODE_UNAUTHORIZED,
                request,
                List.of(resolveMessage(exception, "No autorizado."))
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(
            final IllegalArgumentException exception,
            final HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                CODE_BAD_REQUEST,
                request,
                List.of(resolveMessage(exception, "La solicitud no es válida."))
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception,
            final HttpServletRequest request
    ) {
        final List<String> messages = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage() == null
                        ? "El campo no es válido."
                        : fieldError.getDefaultMessage())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                CODE_VALIDATION_ERROR,
                request,
                messages.isEmpty() ? List.of("La solicitud contiene datos inválidos.") : messages
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalStateException(
            final IllegalStateException exception,
            final HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                CODE_INTERNAL_ERROR,
                request,
                List.of(resolveMessage(exception, "No fue posible completar la operación."))
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
            final Exception exception,
            final HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected Error",
                CODE_UNEXPECTED_ERROR,
                request,
                List.of("Ocurrió un error inesperado. Intenta nuevamente o contacta al administrador.")
        );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            final HttpStatus status,
            final String error,
            final String code,
            final HttpServletRequest request,
            final List<String> messages
    ) {
        final ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                code,
                request.getRequestURI(),
                messages
        );

        return ResponseEntity.status(status).body(response);
    }

    private String resolveMessage(final Exception exception, final String fallbackMessage) {
        if (exception.getMessage() == null || exception.getMessage().isBlank()) {
            return fallbackMessage;
        }

        return exception.getMessage();
    }
}
