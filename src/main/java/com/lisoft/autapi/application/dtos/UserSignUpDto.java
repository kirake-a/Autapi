package com.lisoft.autapi.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserSignUpDto(
    @NotBlank String name,
    @NotBlank String lastName,
    @NotBlank @Email String email,
    Integer age,
    String address,
    String username,
    String phoneNumber,
    @NotBlank @Size(min=8) String password,
    String profilePhotoUrl
) {}
