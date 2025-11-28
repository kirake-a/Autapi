package com.lisoft.autapi.infrastructure.repositories.impl;

import java.util.List;
import java.util.Optional;

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
    public Optional<User> getUserByUserId(String userId) {
        return userRepository.findById(userId)
                .map(UserMapper::toModel);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email))
                .map(UserMapper::toModel);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .map(UserMapper::toModel);
    }

    @Override
    public User saveUser(User user) {
        UserSchema userSchema = userRepository.save(UserMapper.toSchema(user));

        return UserMapper.toModel(userSchema);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toModel)
                .toList();
    }

    @Override
    public User deleteUserById(String userId) {
        Optional<UserSchema> existing = userRepository.findById(userId);

        if (existing.isPresent()) {
            User user = UserMapper.toModel(existing.get());
            userRepository.deleteById(userId);
            return user;
        }

        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

}
