package com.lisoft.autapi.application.repositories;

import com.lisoft.autapi.domain.models.User;

public interface UserRepository {
    User getUserByUserId(String userId);
    User getUserByEmail(String email);
    User saveUser(User user);
    User getUserByUsername(String username);
}
