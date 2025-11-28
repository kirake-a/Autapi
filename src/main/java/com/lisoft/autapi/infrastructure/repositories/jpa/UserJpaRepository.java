package com.lisoft.autapi.infrastructure.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lisoft.autapi.infrastructure.schemas.UserSchema;

@Repository
public interface UserJpaRepository extends JpaRepository<UserSchema, String> {
    UserSchema findByEmail(String email);

    UserSchema findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
