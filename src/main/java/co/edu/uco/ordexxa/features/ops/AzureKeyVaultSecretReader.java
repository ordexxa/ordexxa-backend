package co.edu.uco.ordexxa.features.ops;

import com.azure.identity.ClientCertificateCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AzureKeyVaultSecretReader {

    private final boolean enabled;
    private final String endpoint;
    private final String tenantId;
    private final String clientId;
    private final String clientCertificatePath;

    private SecretClient secretClient;

    public AzureKeyVaultSecretReader(
            @Value("${ordexxa.vault.enabled:false}") boolean enabled,
            @Value("${ordexxa.vault.endpoint:}") String endpoint,
            @Value("${ordexxa.vault.tenant-id:}") String tenantId,
            @Value("${ordexxa.vault.client-id:}") String clientId,
            @Value("${ordexxa.vault.client-certificate-path:}") String clientCertificatePath
    ) {
        this.enabled = enabled;
        this.endpoint = endpoint;
        this.tenantId = tenantId;
        this.clientId = clientId;
        this.clientCertificatePath = clientCertificatePath;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isConfigured() {
        return enabled
                && !isBlank(endpoint)
                && !isBlank(tenantId)
                && !isBlank(clientId)
                && !isBlank(clientCertificatePath);
    }

    public String safeEndpoint() {
        return endpoint == null ? "" : endpoint;
    }

    public Optional<String> readSecret(final String secretName) {
        if (!isConfigured() || isBlank(secretName)) {
            return Optional.empty();
        }

        try {
            final String value = getClient().getSecret(secretName).getValue();

            if (isBlank(value)) {
                return Optional.empty();
            }

            return Optional.of(value);
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }

    private SecretClient getClient() {
        if (secretClient == null) {
            var credential = new ClientCertificateCredentialBuilder()
                    .tenantId(tenantId)
                    .clientId(clientId)
                    .pemCertificate(clientCertificatePath)
                    .build();

            secretClient = new SecretClientBuilder()
                    .vaultUrl(endpoint)
                    .credential(credential)
                    .buildClient();
        }

        return secretClient;
    }

    private static boolean isBlank(final String value) {
        return value == null || value.isBlank();
    }
}
