package com.lisoft.autapi.application.repositories;

import com.lisoft.autapi.domain.models.RoleCatalog;

public interface RoleCatalogRepository {
    RoleCatalog saveRoleCatalog(RoleCatalog roleCatalog);

    RoleCatalog getRoleByType(String type);
}
