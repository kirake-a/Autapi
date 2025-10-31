package com.lisoft.autapi.application.dtos;

public record SuccessfulPasswordResetDto(
        String email,
        String message
) {}
