package com.lisoft.autapi.infrastructure.seeds;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.lisoft.autapi.domain.models.RoleCatalog;
import com.lisoft.autapi.domain.utils.RoleCatalogEnum;
import com.lisoft.autapi.infrastructure.repositories.impl.RoleCatalogRepositoryImpl;

@Component
public class RoleCatalogSeed implements CommandLineRunner {
    private final RoleCatalogRepositoryImpl repository;

    public RoleCatalogSeed(RoleCatalogRepositoryImpl repository) {
        this.repository = repository;  
    }

    @Override
    public void run(String... args) {
        RoleCatalog adminRole = new RoleCatalog(
            RoleCatalogEnum.ADMIN.getId(),
            RoleCatalogEnum.ADMIN.getType(),
            RoleCatalogEnum.ADMIN.getIsActive(),
            RoleCatalogEnum.ADMIN.getDescription()
        );

        RoleCatalog normalUserRole = new RoleCatalog(
            RoleCatalogEnum.NORMAL_USER.getId(),
            RoleCatalogEnum.NORMAL_USER.getType(),
            RoleCatalogEnum.NORMAL_USER.getIsActive(),
            RoleCatalogEnum.NORMAL_USER.getDescription()
        );

        repository.saveRoleCatalog(adminRole);
        repository.saveRoleCatalog(normalUserRole);
    }

}
