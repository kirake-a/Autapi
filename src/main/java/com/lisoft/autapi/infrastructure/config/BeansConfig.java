package com.lisoft.autapi.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lisoft.autapi.application.repositories.RoleCatalogRepository;
import com.lisoft.autapi.application.repositories.UserRepository;
import com.lisoft.autapi.application.services.AuthServiceImpl;

@Configuration
public class BeansConfig {
    @Bean
    public AuthServiceImpl authService(
        PasswordEncoder passwordEncoder,
        UserRepository userRepository,
        RoleCatalogRepository roleRepository
    ) {
        return new AuthServiceImpl(
            passwordEncoder,
            userRepository,
            roleRepository
        );
    }
}
