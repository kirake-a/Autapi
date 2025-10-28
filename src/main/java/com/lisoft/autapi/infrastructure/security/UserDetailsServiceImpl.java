package com.lisoft.autapi.infrastructure.security;

import com.lisoft.autapi.application.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.lisoft.autapi.domain.models.User user = userRepository.getUserByUsername(username);

        if (user == null) {
            user = userRepository.getUserByEmail(username);
        }

        if (user == null) {
            logger.error("Usuario no encontrado: {}", username);
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        return org.springframework.security.core.userdetails.User
            .withUsername(user.username())
            .password(user.password())
            .authorities(new SimpleGrantedAuthority("ROLE_" + user.role().type().toUpperCase()))
            .accountLocked(false)
            .disabled(false)
            .build();
    }
}
