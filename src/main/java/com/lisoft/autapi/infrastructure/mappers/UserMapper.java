package com.lisoft.autapi.infrastructure.mappers;

import com.lisoft.autapi.domain.models.User;
import com.lisoft.autapi.infrastructure.schemas.UserSchema;

public class UserMapper {
    private UserMapper() {}

    public static User toModel(UserSchema schema) {
        if (schema == null) {
            return null;
        }
        
        return new User(
            schema.getId(),
            schema.getName(),
            schema.getLastName(),
            schema.getEmail(),
            schema.getAge(),
            schema.getAddress(),
            schema.getUsername(),
            schema.getPhoneNumber(),
            schema.getPassword(),
            schema.getProfilePhotoUrl(),
            RoleCatalogMapper.toModel(schema.getRole())
        );
    }

    public static UserSchema toSchema(User user) {
        if (user == null) {
            return null;
        }
        
        return new UserSchema(
            user.id(),
            user.name(),
            user.lastName(),
            user.email(),
            user.age(),
            user.address(),
            user.username(),
            user.phoneNumber(),
            user.password(),
            user.profilePhotoUrl(),
            RoleCatalogMapper.toSchema(user.role())
        );
    }

}
