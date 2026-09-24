package com.vannguyen.java_learn_ecom.modules.auth.domain;

import java.time.Instant;

public class UserAccount {

    private final Long id;
    private final String email;
    private final String passwordHash;
    private final UserRole role;
    private final boolean active;
    private final Instant createdAt;

    public UserAccount(
            Long id,
            String email,
            String passwordHash,
            UserRole role,
            boolean active,
            Instant createdAt
    ) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be blank");
        }

        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash must not be blank");
        }

        if (role == null) {
            throw new IllegalArgumentException("Role must not be null");
        }

        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}