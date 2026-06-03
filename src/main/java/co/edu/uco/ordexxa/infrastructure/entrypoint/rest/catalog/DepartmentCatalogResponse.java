package co.edu.uco.ordexxa.infrastructure.entrypoint.rest.catalog;

import java.util.List;

public record DepartmentCatalogResponse(
        String code,
        String name,
        List<CityCatalogResponse> cities
) {
}
