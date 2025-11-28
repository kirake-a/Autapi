package com.lisoft.autapi.infrastructure.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lisoft.autapi.infrastructure.schemas.RoleCatalogSchema;

@Repository
public interface RoleCatalogJpaRepository extends JpaRepository<RoleCatalogSchema, Integer> {
    RoleCatalogSchema findByType(String type);
}
