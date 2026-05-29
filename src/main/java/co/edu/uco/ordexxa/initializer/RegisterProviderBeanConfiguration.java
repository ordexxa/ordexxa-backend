package co.edu.uco.ordexxa.initializer;

import co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport.RegisterProviderInputPort;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.DocumentTypeRepositoryPort;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.MessageCatalogPort;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.ProviderRepositoryPort;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.impl.RegisterProviderUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegisterProviderBeanConfiguration {

    @Bean
    public RegisterProviderInputPort registerProviderInputPort(
            final ProviderRepositoryPort providerRepositoryPort,
            final DocumentTypeRepositoryPort documentTypeRepositoryPort,
            final MessageCatalogPort messageCatalogPort
    ) {
        return new RegisterProviderUseCaseImpl(
                providerRepositoryPort,
                documentTypeRepositoryPort,
                messageCatalogPort
        );
    }
}
