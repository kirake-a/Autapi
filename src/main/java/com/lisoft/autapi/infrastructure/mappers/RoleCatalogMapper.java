package com.lisoft.autapi.infrastructure.mappers;

import com.lisoft.autapi.domain.models.RoleCatalog;
import com.lisoft.autapi.infrastructure.schemas.RoleCatalogSchema;

public class RoleCatalogMapper {
    private RoleCatalogMapper() {}

    public static RoleCatalog toModel(RoleCatalogSchema schema) {
        return new RoleCatalog(
            schema.getId(),
            schema.getType(),
            schema.getIsActive(),
            schema.getDescription()
        );
    }

    public static RoleCatalogSchema toSchema(RoleCatalog model) {
        return new RoleCatalogSchema(
            model.id(),
            model.type(),
            model.isActive(),
            model.description()
        );
    }

}
