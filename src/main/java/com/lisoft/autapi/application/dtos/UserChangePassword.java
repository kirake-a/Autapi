package com.lisoft.autapi.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserChangePassword(
        @NotBlank String currentPassword,
        @NotBlank @Size(min=8) String newPassword
) {
}
