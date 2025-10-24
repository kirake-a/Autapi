package com.lisoft.autapi.infrastructure.seeds;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.lisoft.autapi.domain.models.RoleCatalog;
import com.lisoft.autapi.domain.utils.RoleCatalogEnum;
import com.lisoft.autapi.infrastructure.repositories.impl.RoleCatalogRepositoryImpl;
import java.util.Objects;

@Component
public class RoleCatalogSeed implements CommandLineRunner {
    private final RoleCatalogRepositoryImpl repository;

    public RoleCatalogSeed(RoleCatalogRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        RoleCatalog existingAdminRole = repository.getRoleByType(RoleCatalogEnum.ADMIN.getType());
        RoleCatalog existingUserRole = repository.getRoleByType(RoleCatalogEnum.NORMAL_USER.getType());

        RoleCatalog adminRole = adminRoleCatalogExists(Objects.nonNull(existingAdminRole));
        RoleCatalog normalUserRole = userRoleCatalogExists(Objects.nonNull(existingUserRole));

        repository.saveRoleCatalog(adminRole);
        repository.saveRoleCatalog(normalUserRole);
    }

    public RoleCatalog adminRoleCatalogExists(boolean exists) {
        return exists ? new RoleCatalog(
                RoleCatalogEnum.ADMIN.getId(),
                RoleCatalogEnum.ADMIN.getType(),
                RoleCatalogEnum.ADMIN.getIsActive(),
                RoleCatalogEnum.ADMIN.getDescription())
                : new RoleCatalog(
                        null,
                        RoleCatalogEnum.ADMIN.getType(),
                        RoleCatalogEnum.ADMIN.getIsActive(),
                        RoleCatalogEnum.ADMIN.getDescription());
    }

    public RoleCatalog userRoleCatalogExists(boolean exists) {
        return exists ? new RoleCatalog(
                RoleCatalogEnum.NORMAL_USER.getId(),
                RoleCatalogEnum.NORMAL_USER.getType(),
                RoleCatalogEnum.NORMAL_USER.getIsActive(),
                RoleCatalogEnum.NORMAL_USER.getDescription())
                : new RoleCatalog(
                        null,
                        RoleCatalogEnum.NORMAL_USER.getType(),
                        RoleCatalogEnum.NORMAL_USER.getIsActive(),
                        RoleCatalogEnum.NORMAL_USER.getDescription());
    }
}