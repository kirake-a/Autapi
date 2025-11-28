package com.lisoft.autapi.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserResetPassword(
        @NotBlank @Email String email,
        @NotBlank @Size(min=8) String newPassword,
        @NotBlank @Size(min=8) String newPasswordConfirm
) {}
