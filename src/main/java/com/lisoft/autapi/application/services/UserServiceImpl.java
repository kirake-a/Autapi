package com.lisoft.autapi.application.services;

import static com.lisoft.autapi.domain.utils.Constants.USER_NOT_FOUND;
import static com.lisoft.autapi.domain.utils.Constants.RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.lisoft.autapi.application.dtos.UserChangePassword;
import com.lisoft.autapi.application.dtos.UserUpdateProfileDto;
import com.lisoft.autapi.application.repositories.RoleCatalogRepository;
import com.lisoft.autapi.application.repositories.UserRepository;
import com.lisoft.autapi.application.services.interfaces.UserServiceInterface;
import com.lisoft.autapi.domain.exceptions.ResourceNotFoundException;
import com.lisoft.autapi.domain.models.RoleCatalog;
import com.lisoft.autapi.domain.models.User;

public class UserServiceImpl implements UserServiceInterface {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleCatalogRepository roleRepository;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder encoder,
            RoleCatalogRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUser(String userId) {
        return userRepository.getUserByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public User getMe(String authenticatedUsername) {
        return userRepository.getUserByUsername(authenticatedUsername)
                .orElseThrow(() -> {
                    String error = RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE + USER_NOT_FOUND
                            + " while trying to get user profile.";
                    logger.error(error);
                    return new ResourceNotFoundException(error);
                });
    }

    @Override
    @Transactional
    public User changeUserRole(String userId, String newRole) {
        User user = userRepository.getUserByUserId(userId)
                .orElseThrow(() -> {
                    String error = RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE + USER_NOT_FOUND
                            + " while trying to change user role.";
                    logger.error(error);
                    return new ResourceNotFoundException(error);
                });

        RoleCatalog role = roleRepository.getRoleByType(newRole);

        if (Objects.isNull(role)) {
            String error = RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE + "RoleCatalog with type " + newRole
                    + " not found while trying to change user role.";
            logger.error(error);
            throw new ResourceNotFoundException(error);
        }

        User updatedUser = new User(
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
                role);

        return userRepository.saveUser(updatedUser);
    }

    @Override
    @Transactional
    public User updateUserProfile(String username, UserUpdateProfileDto userUpdate) {
        User user = userRepository.getUserByUsername(username)
                .orElseThrow(() -> {
                    String error = RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE + USER_NOT_FOUND
                            + " while trying to update user profile.";
                    logger.error(error);
                    return new ResourceNotFoundException(error);
                });

        User updatedUser = new User(
                user.id(),
                Objects.nonNull(userUpdate.name()) ? userUpdate.name() : user.name(),
                Objects.nonNull(userUpdate.lastName()) ? userUpdate.lastName() : user.lastName(),
                user.email(),
                Objects.nonNull(userUpdate.age()) ? userUpdate.age() : user.age(),
                Objects.nonNull(userUpdate.address()) ? userUpdate.address() : user.address(),
                user.username(),
                Objects.nonNull(userUpdate.phoneNumber()) ? userUpdate.phoneNumber() : user.phoneNumber(),
                user.password(),
                Objects.nonNull(userUpdate.profilePhotoUrl()) ? userUpdate.profilePhotoUrl() : user.profilePhotoUrl(),
                user.role());

        return userRepository.saveUser(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(String authenticatedUsername, UserChangePassword body) {
        User user = userRepository.getUserByUsername(authenticatedUsername)
                .orElseThrow(() -> {
                    String error = RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE + USER_NOT_FOUND
                            + " while trying to change password.";
                    logger.error(error);
                    return new ResourceNotFoundException(error);
                });

        if (!encoder.matches(body.currentPassword(), user.password())) {
            String error = "Current password is incorrect while trying to change password.";
            logger.error(error);
            throw new BadCredentialsException(error);
        }

        String encodedPassword = encoder.encode(body.newPassword());

        User updatedUser = new User(
                user.id(),
                user.name(),
                user.lastName(),
                user.email(),
                user.age(),
                user.address(),
                user.username(),
                user.phoneNumber(),
                encodedPassword,
                user.profilePhotoUrl(),
                user.role());

        userRepository.saveUser(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public User deleteUser(String userId) {
        User userDeleted = userRepository.deleteUserById(userId);

        if (Objects.isNull(userDeleted)) {
            String error = RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE + USER_NOT_FOUND
                    + " while trying to delete user.";
            logger.error(error);
            throw new ResourceNotFoundException(error);
        }

        return userDeleted;
    }

}
