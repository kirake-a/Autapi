package com.lisoft.autapi.application.dtos;

public record AuthenticationDto(
    String id,
    String fullName,
    String email,
    String token
) {}
