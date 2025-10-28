package com.lisoft.autapi.application.services;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.lisoft.autapi.application.dtos.SuccessfulRegistrationDto;
import com.lisoft.autapi.application.dtos.UserLogInDto;
import com.lisoft.autapi.application.dtos.UserLoginServiceDto;
import com.lisoft.autapi.application.dtos.UserSignUpDto;
import com.lisoft.autapi.application.repositories.RoleCatalogRepository;
import com.lisoft.autapi.application.repositories.UserRepository;
import com.lisoft.autapi.application.services.interfaces.AuthServiceInterface;
import com.lisoft.autapi.domain.exceptions.ConflictWithExistingResourcesException;
import com.lisoft.autapi.domain.exceptions.ResourceNotFoundException;
import com.lisoft.autapi.domain.models.RoleCatalog;
import com.lisoft.autapi.domain.models.User;
import static com.lisoft.autapi.domain.utils.Constants.CONFLICT_WITH_EXISTING_RESOURCES_EXCEPTION_MESSAGE;
import static com.lisoft.autapi.domain.utils.Constants.USER_NOT_FOUND;
import static com.lisoft.autapi.domain.utils.Constants.RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE;
import com.lisoft.autapi.domain.utils.RoleCatalogEnum;

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
        AuthenticationManager authenticationManager
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public UserLoginServiceDto logIn(UserLogInDto user) {
        User existingUser = this.userRepository.getUserByEmail(user.email());

        if (Objects.isNull(existingUser)) {
            String error = RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE + USER_NOT_FOUND + " while trying to log in.";
            logger.error(error);
            throw new ResourceNotFoundException(error);
        }

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.email(), user.password())
        );

        return new UserLoginServiceDto(
            existingUser.id(),
            existingUser.name() + " " + existingUser.lastName(),
            existingUser.email(),
            existingUser
        );
    }

    @Override
    @Transactional
    public SuccessfulRegistrationDto signUp(UserSignUpDto user) {
        if (Objects.nonNull(userRepository.getUserByEmail(user.email()))) {
            String error = CONFLICT_WITH_EXISTING_RESOURCES_EXCEPTION_MESSAGE + "Email already in use";
            logger.error(error);
            throw new ConflictWithExistingResourcesException(error);
        }

        RoleCatalog defaultRole = this.roleRepository.getRoleByType(
            RoleCatalogEnum.NORMAL_USER.getType()
        );

        if (Objects.isNull(defaultRole)) {
            String error = RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE + "Default role not found while trying to sign up.";
            logger.error(error);
            throw new ResourceNotFoundException(error);
        }

        String username = Objects.isNull(user.username()) || user.username().isBlank() ?
            createGenericUsername(user.name(), user.lastName()) :
            user.username();

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
                defaultRole
            )
        );

        return new SuccessfulRegistrationDto(
            userToCreate.id(),
            userToCreate.name() + " " + userToCreate.lastName(),
            userToCreate.email()
        );
    }

    private String createGenericUsername(String name, String lastName) {
        String baseUsername = (name.substring(0, 2) + "." + lastName.substring(0, 3))
            .toLowerCase()
            .replaceAll("\\s+", "");

        String username = baseUsername;
        Integer suffix = Math.toIntExact(System.currentTimeMillis() % 1000);

        while (userRepository.getUserByUsername(username) != null) {
            username = baseUsername + suffix;
            suffix++;
        }

        return username;
    }

}
