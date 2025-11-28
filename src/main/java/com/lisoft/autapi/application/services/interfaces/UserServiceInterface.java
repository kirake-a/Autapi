package com.lisoft.autapi.application.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.lisoft.autapi.application.dtos.UserChangePassword;
import com.lisoft.autapi.application.dtos.UserUpdateProfileDto;
import com.lisoft.autapi.domain.models.User;

public interface UserServiceInterface {
    List<User> getAllUsers();

    Optional<User> getUser(String userId);

    User getMe(String authenticatedUserId);

    User changeUserRole(String userId, String newRole);

    User updateUserProfile(String userId, UserUpdateProfileDto userUpdate);

    void changePassword(String authenticatedUserId, UserChangePassword body);

    boolean usernameExists(String username);

    boolean emailExists(String email);

    User deleteUser(String userId);
}
