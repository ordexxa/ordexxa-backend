package co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport;

import co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport.dto.RegisterProviderRequestDTO;
import co.edu.uco.ordexxa.features.provider.registerprovider.application.inputport.dto.RegisterProviderResponseDTO;

public interface RegisterProviderInputPort {

    RegisterProviderResponseDTO execute(RegisterProviderRequestDTO request);
}
