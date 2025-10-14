package com.lisoft.autapi.infrastructure.repositories.impl;

import java.util.Objects;

import org.springframework.stereotype.Repository;

import com.lisoft.autapi.application.repositories.UserRepository;
import com.lisoft.autapi.domain.models.User;
import com.lisoft.autapi.infrastructure.mappers.UserMapper;
import com.lisoft.autapi.infrastructure.repositories.jpa.UserJpaRepository;
import com.lisoft.autapi.infrastructure.schemas.UserSchema;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userRepository;

    public UserRepositoryImpl(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByUserId(String userId) {
        UserSchema user = userRepository.findById(userId).orElse(null);

        if (Objects.isNull(user)) {
            return null;
        }

        return UserMapper.toModel(user);
    }

    @Override
    public User getUserByEmail(String email) {
        UserSchema user = userRepository.findByEmail(email);

        if (Objects.isNull(user)) {
            return null;
        }

        return UserMapper.toModel(user);
    }

    @Override
    public User saveUser(User user) {
        UserSchema userSchema = userRepository.save(UserMapper.toSchema(user));

        return UserMapper.toModel(userSchema);
    }
}
