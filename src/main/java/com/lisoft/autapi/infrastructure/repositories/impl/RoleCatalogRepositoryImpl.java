package com.lisoft.autapi.infrastructure.repositories.impl;

import org.springframework.stereotype.Repository;

import com.lisoft.autapi.application.repositories.RoleCatalogRepository;
import com.lisoft.autapi.domain.models.RoleCatalog;
import com.lisoft.autapi.infrastructure.mappers.RoleCatalogMapper;
import com.lisoft.autapi.infrastructure.repositories.jpa.RoleCatalogJpaRepository;
import com.lisoft.autapi.infrastructure.schemas.RoleCatalogSchema;

@Repository
public class RoleCatalogRepositoryImpl implements RoleCatalogRepository {
    private final RoleCatalogJpaRepository jpaRepository;

    public RoleCatalogRepositoryImpl(RoleCatalogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public RoleCatalog saveRoleCatalog(RoleCatalog roleCatalog) {
        RoleCatalogSchema schema = this.jpaRepository.save(RoleCatalogMapper.toSchema(roleCatalog));

        return RoleCatalogMapper.toModel(schema);
    }

    @Override
    public RoleCatalog getRoleByType(String type) {
        RoleCatalogSchema schema = this.jpaRepository.findByType(type);

        return schema != null ? RoleCatalogMapper.toModel(schema) : null;
    }

}
