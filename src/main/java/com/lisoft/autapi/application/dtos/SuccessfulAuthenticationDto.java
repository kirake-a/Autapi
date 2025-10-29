package com.lisoft.autapi.application.dtos;

public record SuccessfulAuthenticationDto(
    String id,
    String fullName,
    String email,
    String token
) {}
