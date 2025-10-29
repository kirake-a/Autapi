package com.lisoft.autapi.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lisoft.autapi.application.repositories.RoleCatalogRepository;
import com.lisoft.autapi.application.repositories.UserRepository;
import com.lisoft.autapi.application.services.AuthServiceImpl;
import com.lisoft.autapi.application.services.UserServiceImpl;

@Configuration
public class BeansConfig {
    @Bean
    AuthServiceImpl authService(
        PasswordEncoder passwordEncoder,
        UserRepository userRepository,
        RoleCatalogRepository roleRepository,
        AuthenticationManager authenticationManager
    ) {
        return new AuthServiceImpl(
            passwordEncoder,
            userRepository,
            roleRepository,
            authenticationManager
        );
    }

    @Bean
    UserServiceImpl userService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        RoleCatalogRepository roleRepository
    ) {
        return new UserServiceImpl(
            userRepository,
            passwordEncoder,
            roleRepository
        );
    }
}
