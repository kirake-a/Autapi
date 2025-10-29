package com.lisoft.autapi.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLogInDto(
    @Email String email,
    @NotBlank String password,
    String username
) {

}
