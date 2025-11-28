package com.lisoft.autapi.domain.utils;

import lombok.Getter;

@Getter
public enum RoleCatalogEnum {
    ADMIN(1, "ADMIN", true, "Administrator role"),
    NORMAL_USER(2, "USER", true, "Normal user role");

    private final Integer id;
    private final String type;
    private final Boolean isActive;
    private final String description;

    RoleCatalogEnum(
        Integer id,
        String type,
        Boolean isActive,
        String description
    ) {
        this.id = id;
        this.type = type;
        this.isActive = isActive;
        this.description = description;
    }
}
