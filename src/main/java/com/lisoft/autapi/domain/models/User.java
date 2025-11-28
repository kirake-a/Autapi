package com.lisoft.autapi.domain.models;

public record User(
    String id,
    String name,
    String lastName,
    String email,
    Integer age,
    String address,
    String username,
    String phoneNumber,
    String password,
    String profilePhotoUrl,
    RoleCatalog role
) {}
