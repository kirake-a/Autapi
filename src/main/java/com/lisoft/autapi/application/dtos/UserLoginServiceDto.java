package com.lisoft.autapi.application.dtos;

import com.lisoft.autapi.domain.models.User;

public record UserLoginServiceDto(
    String id,
    String fullName,
    String email,
    User user
) {}
