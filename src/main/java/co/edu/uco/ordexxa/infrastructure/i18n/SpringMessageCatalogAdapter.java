package co.edu.uco.ordexxa.infrastructure.i18n;

import co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase.MessageCatalogPort;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringMessageCatalogAdapter implements MessageCatalogPort {

    private final MessageSource messageSource;

    public SpringMessageCatalogAdapter(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(final String code, final Object... arguments) {
        return messageSource.getMessage(code, arguments, LocaleContextHolder.getLocale());
    }
}
