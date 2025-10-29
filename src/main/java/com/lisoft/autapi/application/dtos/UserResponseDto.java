package com.lisoft.autapi.application.dtos;

public record UserResponseDto(
        String id,
        String name,
        String lastName,
        String email,
        Integer age,
        String address,
        String username,
        String phoneNumber,
        String profilePhotoUrl) {
}
