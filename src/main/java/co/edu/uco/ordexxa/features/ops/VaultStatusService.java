package co.edu.uco.ordexxa.features.ops;

import com.azure.identity.ClientCertificateCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VaultStatusService {

    private final boolean enabled;
    private final String endpoint;
    private final String tenantId;
    private final String clientId;
    private final String clientCertificatePath;
    private final String proofSecretName;

    public VaultStatusService(
            @Value("${ordexxa.vault.enabled:false}") boolean enabled,
            @Value("${ordexxa.vault.endpoint:}") String endpoint,
            @Value("${ordexxa.vault.tenant-id:}") String tenantId,
            @Value("${ordexxa.vault.client-id:}") String clientId,
            @Value("${ordexxa.vault.client-certificate-path:}") String clientCertificatePath,
            @Value("${ordexxa.vault.proof-secret-name:ordexxa-vault-proof}") String proofSecretName
    ) {
        this.enabled = enabled;
        this.endpoint = endpoint;
        this.tenantId = tenantId;
        this.clientId = clientId;
        this.clientCertificatePath = clientCertificatePath;
        this.proofSecretName = proofSecretName;
    }

    public VaultStatusResponse checkStatus() {
        if (!enabled) {
            return new VaultStatusResponse(
                    "Azure Key Vault",
                    false,
                    false,
                    false,
                    safeEndpoint(),
                    proofSecretName,
                    "Vault integration is disabled. The application is running with environment variables."
            );
        }

        if (isBlank(endpoint) || isBlank(tenantId) || isBlank(clientId)
                || isBlank(clientCertificatePath) || isBlank(proofSecretName)) {
            return new VaultStatusResponse(
                    "Azure Key Vault",
                    true,
                    false,
                    false,
                    safeEndpoint(),
                    proofSecretName,
                    "Vault integration is enabled but required configuration is missing."
            );
        }

        try {
            var credential = new ClientCertificateCredentialBuilder()
                    .tenantId(tenantId)
                    .clientId(clientId)
                    .pemCertificate(clientCertificatePath)
                    .build();

            var client = new SecretClientBuilder()
                    .vaultUrl(endpoint)
                    .credential(credential)
                    .buildClient();

            var secret = client.getSecret(proofSecretName);
            var connected = secret != null && !isBlank(secret.getValue());

            return new VaultStatusResponse(
                    "Azure Key Vault",
                    true,
                    true,
                    connected,
                    safeEndpoint(),
                    proofSecretName,
                    connected
                            ? "Vault connection verified. Proof secret was read successfully."
                            : "Vault connection reached, but proof secret value was empty."
            );
        } catch (RuntimeException exception) {
            return new VaultStatusResponse(
                    "Azure Key Vault",
                    true,
                    true,
                    false,
                    safeEndpoint(),
                    proofSecretName,
                    "Vault connection failed: " + exception.getClass().getSimpleName()
            );
        }
    }

    private String safeEndpoint() {
        return endpoint == null ? "" : endpoint;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
