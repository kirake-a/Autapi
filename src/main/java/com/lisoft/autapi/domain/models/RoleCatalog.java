package com.lisoft.autapi.domain.models;

public record RoleCatalog(
    Integer id,
    String type,
    Boolean isActive,
    String description
) {

}
