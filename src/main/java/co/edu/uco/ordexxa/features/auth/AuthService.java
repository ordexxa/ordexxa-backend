package co.edu.uco.ordexxa.features.auth;

import co.edu.uco.ordexxa.features.audit.AuditService;
import co.edu.uco.ordexxa.infrastructure.entrypoint.rest.auth.dto.*;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity.UserAccountEntity;
import co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.repository.UserAccountJpaRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private static final String ROLE_ADMIN = "ADMIN";
    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;
    private static final int ACCOUNT_LOCK_MINUTES = 15;

    private final UserAccountJpaRepository userAccountJpaRepository;
    private final JavaMailSender javaMailSender;
    private final AuditService auditService;
    private final MessageSource messageSource;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom;

    @Value("${ordexxa.auth.verification-code-expiration-minutes:15}")
    private long verificationCodeExpirationMinutes;

    @Value("${ordexxa.mail.from:no-reply@ordexxa.local}")
    private String mailFrom;

    @Value("${ordexxa.auth.show-verification-code-in-console:false}")
    private boolean showVerificationCodeInConsole;

    @Value("${AUTH0_ISSUER_URI:}")
    private String auth0IssuerUri;

    @Value("${AUTH0_AUDIENCE:}")
    private String auth0Audience;

    private JwtDecoder auth0JwtDecoder;

    public AuthService(
            final UserAccountJpaRepository userAccountJpaRepository,
            final JavaMailSender javaMailSender,
            final AuditService auditService,
            final MessageSource messageSource
    ) {
        this.userAccountJpaRepository = userAccountJpaRepository;
        this.javaMailSender = javaMailSender;
        this.auditService = auditService;
        this.messageSource = messageSource;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.secureRandom = new SecureRandom();
    }

    @Transactional
    public RegisterAccountResponse register(final RegisterAccountRequest request) {
        final String normalizedEmail = normalizeEmail(request.email());

        final UserAccountEntity user = userAccountJpaRepository.findByEmail(normalizedEmail)
                .orElseGet(UserAccountEntity::new);

        if (user.getId() != null && Boolean.TRUE.equals(user.getVerified())) {
            auditService.registerFailure(
                    "ACCOUNT_REGISTERED",
                    normalizedEmail,
                    null,
                    "USER_ACCOUNT",
                    normalizedEmail,
                    "Intento de registro con una cuenta ya verificada."
            );
            throw new IllegalArgumentException(message("ordexxa.auth.account.verified.duplicated"));
        }

        final String verificationCode = generateVerificationCode();

        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
            user.setCreatedAt(LocalDateTime.now());
        }

        user.setFullName(request.fullName().trim());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(ROLE_ADMIN);
        user.setVerificationCode(verificationCode);
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(verificationCodeExpirationMinutes));
        user.setVerified(Boolean.FALSE);
        user.setVerifiedAt(null);
        user.setCurrentToken(null);
        user.setTokenIssuedAt(null);
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);

        userAccountJpaRepository.save(user);
        sendVerificationEmail(user.getEmail(), user.getFullName(), verificationCode);

        auditService.registerSuccess(
                "ACCOUNT_REGISTERED",
                user.getEmail(),
                user.getRole(),
                "USER_ACCOUNT",
                user.getId().toString(),
                "Cuenta creada y correo de verificación enviado."
        );

        if (showVerificationCodeInConsole) {
            System.out.println("Código de verificación Ordexxa para " + user.getEmail() + ": " + verificationCode);
        }

        return new RegisterAccountResponse(
                user.getEmail(),
                message("ordexxa.auth.account.created")
        );
    }

    @Transactional
    public VerifyAccountResponse verify(final VerifyAccountRequest request) {
        final String normalizedEmail = normalizeEmail(request.email());

        final UserAccountEntity user = userAccountJpaRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException(message("ordexxa.auth.account.notFound")));

        if (Boolean.TRUE.equals(user.getVerified())) {
            return new VerifyAccountResponse(user.getEmail(), message("ordexxa.auth.account.alreadyVerified"));
        }

        if (user.getVerificationCodeExpiresAt() == null || user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            auditService.registerFailure(
                    "ACCOUNT_VERIFIED",
                    user.getEmail(),
                    user.getRole(),
                    "USER_ACCOUNT",
                    user.getId().toString(),
                    "Código de verificación expirado."
            );
            throw new IllegalArgumentException(message("ordexxa.auth.verificationCode.expired"));
        }

        if (!request.code().equals(user.getVerificationCode())) {
            auditService.registerFailure(
                    "ACCOUNT_VERIFIED",
                    user.getEmail(),
                    user.getRole(),
                    "USER_ACCOUNT",
                    user.getId().toString(),
                    "Código de verificación inválido."
            );
            throw new IllegalArgumentException(message("ordexxa.auth.verificationCode.invalid"));
        }

        user.setVerified(Boolean.TRUE);
        user.setVerifiedAt(LocalDateTime.now());
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);

        userAccountJpaRepository.save(user);

        auditService.registerSuccess(
                "ACCOUNT_VERIFIED",
                user.getEmail(),
                user.getRole(),
                "USER_ACCOUNT",
                user.getId().toString(),
                "Cuenta verificada exitosamente."
        );

        return new VerifyAccountResponse(user.getEmail(), message("ordexxa.auth.account.verified"));
    }

    @Transactional
    public LoginResponse login(final LoginRequest request) {
        final String normalizedEmail = normalizeEmail(request.email());

        final Optional<UserAccountEntity> optionalUser = userAccountJpaRepository.findByEmail(normalizedEmail);

        if (optionalUser.isEmpty()) {
            auditService.registerFailure(
                    "LOGIN_FAILED",
                    normalizedEmail,
                    null,
                    "USER_ACCOUNT",
                    normalizedEmail,
                    "Intento de inicio de sesión con correo inexistente."
            );
            throw new IllegalArgumentException(message("ordexxa.auth.credentials.invalid"));
        }

        final UserAccountEntity user = optionalUser.get();

        validateAccountIsNotLocked(user);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            registerFailedLoginAttempt(user);

            auditService.registerFailure(
                    "LOGIN_FAILED",
                    user.getEmail(),
                    user.getRole(),
                    "USER_ACCOUNT",
                    user.getId().toString(),
                    "Contraseña inválida."
            );

            throw new IllegalArgumentException(message("ordexxa.auth.credentials.invalid"));
        }

        if (!Boolean.TRUE.equals(user.getVerified())) {
            auditService.registerFailure(
                    "LOGIN_FAILED",
                    user.getEmail(),
                    user.getRole(),
                    "USER_ACCOUNT",
                    user.getId().toString(),
                    "Cuenta no verificada."
            );
            throw new IllegalArgumentException(message("ordexxa.auth.account.notVerified"));
        }

        final String token = UUID.randomUUID().toString();

        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        user.setCurrentToken(token);
        user.setTokenIssuedAt(LocalDateTime.now());

        userAccountJpaRepository.save(user);

        auditService.registerSuccess(
                "LOGIN_SUCCESS",
                user.getEmail(),
                user.getRole(),
                "USER_ACCOUNT",
                user.getId().toString(),
                "Inicio de sesión exitoso."
        );

        return new LoginResponse(
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                token,
                message("ordexxa.auth.login.success")
        );
    }

    @Transactional
    public LogoutResponse logout(final String authorizationHeader) {
        final UserAccountEntity user = validateAuthenticatedToken(authorizationHeader);

        user.setCurrentToken(null);
        user.setTokenIssuedAt(null);

        userAccountJpaRepository.save(user);

        auditService.registerSuccess(
                "LOGOUT",
                user.getEmail(),
                user.getRole(),
                "USER_ACCOUNT",
                user.getId().toString(),
                "Cierre de sesión exitoso."
        );

        return new LogoutResponse(message("ordexxa.auth.logout.success"));
    }

    @Transactional(readOnly = true)
    public UserAccountEntity validateAuthenticatedToken(final String authorizationHeader) {
        final String token = extractBearerToken(authorizationHeader);

        final Optional<UserAccountEntity> auth0User = validateAuth0Token(token);
        if (auth0User.isPresent()) {
            return auth0User.get();
        }

        final UserAccountEntity user = userAccountJpaRepository.findByCurrentToken(token)
                .orElseThrow(() -> new SecurityException(message("ordexxa.security.token.invalidOrExpired")));

        if (!Boolean.TRUE.equals(user.getVerified())) {
            throw new SecurityException(message("ordexxa.security.account.notVerified"));
        }

        validateAccountIsNotLocked(user);

        return user;
    }

    @Transactional(readOnly = true)
    public UserAccountEntity validateAdminToken(final String authorizationHeader) {
        final UserAccountEntity user = validateAuthenticatedToken(authorizationHeader);

        if (!ROLE_ADMIN.equals(user.getRole())) {
            throw new SecurityException(message("ordexxa.security.forbidden"));
        }

        return user;
    }

    private Optional<UserAccountEntity> validateAuth0Token(final String token) {
        if (!isAuth0Enabled() || !isJwtToken(token)) {
            return Optional.empty();
        }

        try {
            final Jwt jwt = getAuth0JwtDecoder().decode(token);
            validateAuth0Audience(jwt);

            final String subject = jwt.getSubject();
            final String email = firstNonBlank(
                    jwt.getClaimAsString("email"),
                    subject + "@auth0.ordexxa.local"
            );
            final String fullName = firstNonBlank(
                    jwt.getClaimAsString("name"),
                    jwt.getClaimAsString("nickname"),
                    email
            );

            final UserAccountEntity user = new UserAccountEntity();
            user.setId(UUID.nameUUIDFromBytes(("auth0:" + subject).getBytes(StandardCharsets.UTF_8)));
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPasswordHash("AUTH0_EXTERNAL_IDENTITY");
            user.setRole(ROLE_ADMIN);
            user.setVerified(Boolean.TRUE);
            user.setCreatedAt(LocalDateTime.now());
            user.setVerifiedAt(LocalDateTime.now());
            user.setFailedLoginAttempts(0);
            user.setLockedUntil(null);
            user.setCurrentToken(null);
            user.setTokenIssuedAt(null);

            return Optional.of(user);
        } catch (JwtException | IllegalArgumentException exception) {
            throw new SecurityException(message("ordexxa.security.token.invalidOrExpired"), exception);
        }
    }

    private boolean isAuth0Enabled() {
        return auth0IssuerUri != null
                && !auth0IssuerUri.isBlank()
                && auth0Audience != null
                && !auth0Audience.isBlank();
    }

    private boolean isJwtToken(final String token) {
        return token != null && token.split("\\.", -1).length == 3;
    }

    private JwtDecoder getAuth0JwtDecoder() {
        if (auth0JwtDecoder == null) {
            auth0JwtDecoder = NimbusJwtDecoder.withJwkSetUri(normalizedAuth0IssuerUri() + ".well-known/jwks.json").build();
        }

        return auth0JwtDecoder;
    }

    private String normalizedAuth0IssuerUri() {
        String issuer = auth0IssuerUri.trim();

        if (!issuer.endsWith("/")) {
            issuer = issuer + "/";
        }

        return issuer;
    }

    private void validateAuth0Audience(final Jwt jwt) {
        final List<String> audiences = jwt.getAudience();

        if (audiences == null || !audiences.contains(auth0Audience)) {
            throw new SecurityException(message("ordexxa.security.token.invalidOrExpired"));
        }
    }

    private String firstNonBlank(final String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }

        return "Usuario Auth0";
    }

    private void validateAccountIsNotLocked(final UserAccountEntity user) {
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            auditService.registerFailure(
                    "LOGIN_FAILED",
                    user.getEmail(),
                    user.getRole(),
                    "USER_ACCOUNT",
                    user.getId().toString(),
                    "Cuenta bloqueada temporalmente por intentos fallidos."
            );
            throw new SecurityException(message("ordexxa.security.account.locked"));
        }
    }

    private void registerFailedLoginAttempt(final UserAccountEntity user) {
        final int currentAttempts = user.getFailedLoginAttempts() == null ? 0 : user.getFailedLoginAttempts();
        final int updatedAttempts = currentAttempts + 1;

        user.setFailedLoginAttempts(updatedAttempts);

        if (updatedAttempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
            user.setLockedUntil(LocalDateTime.now().plusMinutes(ACCOUNT_LOCK_MINUTES));
        }

        userAccountJpaRepository.save(user);
    }

    private String extractBearerToken(final String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.trim().isEmpty()) {
            throw new SecurityException(message("ordexxa.security.token.required"));
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new SecurityException(message("ordexxa.security.token.bearerFormat"));
        }

        final String token = authorizationHeader.substring("Bearer ".length()).trim();

        if (token.isEmpty()) {
            throw new SecurityException(message("ordexxa.security.token.empty"));
        }

        return token;
    }

    private String normalizeEmail(final String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(message("ordexxa.auth.email.required"));
        }

        return email.trim().toLowerCase();
    }

    private String message(final String code, final Object... arguments) {
        return messageSource.getMessage(code, arguments, LocaleContextHolder.getLocale());
    }

    private String generateVerificationCode() {
        final int number = secureRandom.nextInt(1_000_000);
        return String.format("%06d", number);
    }

    private void sendVerificationEmail(
            final String email,
            final String fullName,
            final String verificationCode
    ) {
        try {
            final MimeMessage message = javaMailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setFrom(mailFrom);
            helper.setTo(email);
            helper.setSubject("Código de verificación Ordexxa");
            helper.setText("""
                    Hola %s,

                    Tu código de verificación para Ordexxa es:

                    %s

                    Este código vence en %d minutos.

                    Ordexxa
                    """.formatted(fullName, verificationCode, verificationCodeExpirationMinutes), false);

            javaMailSender.send(message);
        } catch (MessagingException exception) {
            throw new IllegalStateException(message("ordexxa.notification.verificationEmail.buildError"), exception);
        }
    }
}
