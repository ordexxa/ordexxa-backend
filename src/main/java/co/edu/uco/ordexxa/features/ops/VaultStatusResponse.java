package co.edu.uco.ordexxa.features.ops;

public record VaultStatusResponse(
        String provider,
        boolean enabled,
        boolean configured,
        boolean connected,
        String vaultEndpoint,
        String proofSecretName,
        String message
) {
}
