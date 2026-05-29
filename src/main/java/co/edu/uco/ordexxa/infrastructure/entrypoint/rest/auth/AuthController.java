package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.auth;

import co.edu.uco.ordexxa.features.auth.AuthService;
import co.edu.uco.ordexxa.infrastructure.entrypoint.rest.auth.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterAccountResponse> register(
            @Valid @RequestBody final RegisterAccountRequest request
    ) {
        final RegisterAccountResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyAccountResponse> verify(
            @Valid @RequestBody final VerifyAccountRequest request
    ) {
        final VerifyAccountResponse response = authService.verify(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody final LoginRequest request
    ) {
        final LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(
            @RequestHeader(value = "Authorization", required = false) final String authorizationHeader
    ) {
        final LogoutResponse response = authService.logout(authorizationHeader);
        return ResponseEntity.ok(response);
    }
}
