package com.lisoft.autapi.application.utils;

import com.lisoft.autapi.application.repositories.UserRepository;

public class GenericUsernameGenerator {
    public static String create(String name, String lastName, UserRepository userRepository) {
        String baseUsername = (name.substring(0, 2) + "." + lastName.substring(0, 3))
            .toLowerCase()
            .replaceAll("\\s+", "");

        String username = baseUsername;
        int suffix = Math.toIntExact(System.currentTimeMillis() % 1000);

        while (userRepository.existsByUsername(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        return username;
    }
}
