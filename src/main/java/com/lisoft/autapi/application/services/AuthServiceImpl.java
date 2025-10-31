package com.lisoft.autapi.application.services;

import java.util.Objects;

import com.lisoft.autapi.application.dtos.*;
import com.lisoft.autapi.application.utils.GenericUsernameGenerator;
import com.lisoft.autapi.application.utils.PasswordValidator;
import com.lisoft.autapi.domain.exceptions.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.lisoft.autapi.application.repositories.RoleCatalogRepository;
import com.lisoft.autapi.application.repositories.UserRepository;
import com.lisoft.autapi.application.services.interfaces.AuthServiceInterface;
import com.lisoft.autapi.domain.exceptions.ConflictWithExistingResourcesException;
import com.lisoft.autapi.domain.exceptions.ResourceNotFoundException;
import com.lisoft.autapi.domain.models.RoleCatalog;
import com.lisoft.autapi.domain.models.User;
import com.lisoft.autapi.domain.utils.RoleCatalogEnum;

import static com.lisoft.autapi.domain.utils.Constants.*;

public class AuthServiceImpl implements AuthServiceInterface {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleCatalogRepository roleRepository;
    private final AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            RoleCatalogRepository roleRepository,
            AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public UserLoginServiceDto logIn(UserLogInDto user) {
        User existingUser = this.userRepository.getUserByEmail(user.email())
                .orElseThrow(() -> {
                    String error = RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE + USER_NOT_FOUND + " while trying to log in.";
                    logger.error(error);
                    return new ResourceNotFoundException(error);
                });

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.email(), user.password()));

        return new UserLoginServiceDto(
                existingUser.id(),
                existingUser.name() + " " + existingUser.lastName(),
                existingUser.email(),
                existingUser);
    }

    @Override
    @Transactional
    public SuccessfulRegistrationDto signUp(UserSignUpDto user) {
        if (userRepository.existsByEmail(user.email())) {
            String error = CONFLICT_WITH_EXISTING_RESOURCES_EXCEPTION_MESSAGE + "Email already in use";
            logger.error(error);
            throw new ConflictWithExistingResourcesException(error);
        }

        if (!PasswordValidator.isValid(user.password())) {
            logger.error("Sign up: " + PASSWORD_DOES_NOT_FOLLOW_POLICY);
            throw new InvalidArgumentException(PASSWORD_DOES_NOT_FOLLOW_POLICY);
        }

        RoleCatalog defaultRole = this.roleRepository.getRoleByType(
                RoleCatalogEnum.NORMAL_USER.getType());

        if (Objects.isNull(defaultRole)) {
            String error = RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE + "Default role not found while trying to sign up.";
            logger.error(error);
            throw new ResourceNotFoundException(error);
        }

        String username = Objects.isNull(user.username()) || user.username().isBlank()
                ? GenericUsernameGenerator.create(user.name(), user.lastName(), userRepository)
                : user.username();

        User userToCreate = this.userRepository.saveUser(
                new User(
                        null,
                        user.name(),
                        user.lastName(),
                        user.email(),
                        user.age(),
                        user.address(),
                        username,
                        user.phoneNumber(),
                        passwordEncoder.encode(user.password()),
                        user.profilePhotoUrl(),
                        defaultRole));

        return new SuccessfulRegistrationDto(
                userToCreate.id(),
                userToCreate.name() + " " + userToCreate.lastName(),
                userToCreate.email());
    }

    @Override
    @Transactional
    public SuccessfulPasswordResetDto passwordReset(UserResetPassword data) {
        User user = userRepository.getUserByEmail(data.email())
                .orElseThrow(() -> {
                    String error =  RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE + USER_NOT_FOUND + " while trying to reset password.";
                    logger.error(error);
                    return new ResourceNotFoundException(error);
                });

        if (!data.newPassword().equals(data.newPasswordConfirm())) {
            String error = "The passwords do not match";
            logger.error(error);
            throw new InvalidArgumentException(error);
        }

        if (!PasswordValidator.isValid(data.newPassword())) {
            logger.error("Password reset :" + PASSWORD_DOES_NOT_FOLLOW_POLICY);
            throw new InvalidArgumentException(PASSWORD_DOES_NOT_FOLLOW_POLICY);
        }

        String encodedPassword = passwordEncoder.encode(data.newPassword());

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

        User userNewPassword = userRepository.saveUser(updatedUser);

        return new SuccessfulPasswordResetDto(
                userNewPassword.email(),
                "The password was successfully changed"
        );
    }

}
