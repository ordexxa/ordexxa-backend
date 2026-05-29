package co.edu.uco.ordexxa.features.provider.registerprovider.application.usecase;

public interface MessageCatalogPort {

    String getMessage(String code, Object... arguments);
}
