package com.lisoft.autapi.application.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserChangePassword(
        @NotBlank String currentPassword,
        @NotBlank String newPassword
) {
}
