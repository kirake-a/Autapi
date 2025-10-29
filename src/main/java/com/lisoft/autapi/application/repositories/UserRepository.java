package com.lisoft.autapi.application.repositories;

import java.util.List;
import java.util.Optional;

import com.lisoft.autapi.domain.models.User;

public interface UserRepository {
    Optional<User> getUserByUserId(String userId);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);

    User saveUser(User user);

    List<User> getAllUsers();

    User deleteUserById(String userId);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
