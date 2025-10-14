package com.lisoft.autapi.application.services;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.lisoft.autapi.application.dtos.AuthenticationDto;
import com.lisoft.autapi.application.dtos.SuccessfulRegistrationDto;
import com.lisoft.autapi.application.dtos.UserLogInDto;
import com.lisoft.autapi.application.dtos.UserSignUpDto;
import com.lisoft.autapi.application.repositories.RoleCatalogRepository;
import com.lisoft.autapi.application.repositories.UserRepository;
import com.lisoft.autapi.application.services.interfaces.AuthServiceInterface;
import com.lisoft.autapi.domain.exceptions.ConflictWithExistingResourcesException;
import com.lisoft.autapi.domain.exceptions.ResourceNotFoundException;
import com.lisoft.autapi.domain.models.RoleCatalog;
import com.lisoft.autapi.domain.models.User;
import com.lisoft.autapi.domain.utils.RoleCatalogEnum;

public class AuthServiceImpl implements AuthServiceInterface {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleCatalogRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(
        PasswordEncoder passwordEncoder,
        UserRepository userRepository,
        RoleCatalogRepository roleRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public AuthenticationDto logIn(UserLogInDto user) {
        throw new UnsupportedOperationException("Unimplemented method 'logIn'");
    }

    @Override
    @Transactional
    public SuccessfulRegistrationDto signUp(UserSignUpDto user) {
        if (Objects.nonNull(userRepository.getUserByEmail(user.email()))) {
            logger.error("Email already in use");
            throw new ConflictWithExistingResourcesException("Email already in use");
        }

        RoleCatalog defaultRole = this.roleRepository.getRoleByType(
            RoleCatalogEnum.NORMAL_USER.getType()
        );

        if (Objects.isNull(defaultRole)) {
            logger.error("Default role not found");
            throw new ResourceNotFoundException("Default role not found");
        }

        User userToCreate = this.userRepository.saveUser(
            new User(
                null,
                user.name(),
                user.lastName(),
                user.email(),
                user.age(),
                user.address(),
                user.username(),
                user.phoneNumber(),
                passwordEncoder.encode(user.password()),
                user.profilePhotoUrl(),
                defaultRole
            )
        );

        return new SuccessfulRegistrationDto(
            userToCreate.id(),
            userToCreate.name() + " " + userToCreate.lastName(),
            userToCreate.email()
        );
    }

}
