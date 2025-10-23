package com.lisoft.autapi.infrastructure.schemas;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
public class UserSchema implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "age", nullable = true)
    private Integer age;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profile_photo_url", nullable = true)
    private String profilePhotoUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleCatalogSchema role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null || !Boolean.TRUE.equals(role.getIsActive())) {
            return List.of();
        }

        String authority = "ROLE_" + role.getType().toUpperCase();

        return List.of(new SimpleGrantedAuthority(authority));
    }
}
