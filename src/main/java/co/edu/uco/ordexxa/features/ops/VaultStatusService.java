package co.edu.uco.ordexxa.features.ops;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VaultStatusService {

    private final AzureKeyVaultSecretReader secretReader;
    private final String proofSecretName;
    private final String mailPasswordSecretName;

    public VaultStatusService(
            final AzureKeyVaultSecretReader secretReader,
            @Value("${ordexxa.vault.proof-secret-name:ordexxa-vault-proof}") String proofSecretName,
            @Value("${ordexxa.vault.mail-password-secret-name:}") String mailPasswordSecretName
    ) {
        this.secretReader = secretReader;
        this.proofSecretName = proofSecretName;
        this.mailPasswordSecretName = mailPasswordSecretName;
    }

    public VaultStatusResponse checkStatus() {
        if (!secretReader.isEnabled()) {
            return new VaultStatusResponse(
                    "Azure Key Vault",
                    false,
                    false,
                    false,
                    secretReader.safeEndpoint(),
                    proofSecretName,
                    mailPasswordSecretName,
                    !isBlank(mailPasswordSecretName),
                    false,
                    "Vault integration is disabled. The application is running with environment variables."
            );
        }

        if (!secretReader.isConfigured()) {
            return new VaultStatusResponse(
                    "Azure Key Vault",
                    true,
                    false,
                    false,
                    secretReader.safeEndpoint(),
                    proofSecretName,
                    mailPasswordSecretName,
                    !isBlank(mailPasswordSecretName),
                    false,
                    "Vault integration is enabled but required configuration is missing."
            );
        }

        final boolean proofSecretReadable = secretReader.readSecret(proofSecretName).isPresent();
        final boolean mailSecretConfigured = !isBlank(mailPasswordSecretName);
        final boolean mailSecretReadable = mailSecretConfigured
                && secretReader.readSecret(mailPasswordSecretName).isPresent();

        final String message = proofSecretReadable
                ? "Vault connection verified. Proof secret was read successfully."
                : "Vault connection failed. Proof secret could not be read.";

        return new VaultStatusResponse(
                "Azure Key Vault",
                true,
                true,
                proofSecretReadable,
                secretReader.safeEndpoint(),
                proofSecretName,
                mailPasswordSecretName,
                mailSecretConfigured,
                mailSecretReadable,
                message
        );
    }

    private static boolean isBlank(final String value) {
        return value == null || value.isBlank();
    }
}
