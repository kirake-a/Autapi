package com.lisoft.autapi.application.dtos;

public record UserUpdateProfileDto(
    String name,
    String lastName,
    Integer age,
    String address,
    String phoneNumber,
    String profilePhotoUrl
) {}
