package com.vannguyen.java_learn_ecom.modules.auth.application;

import com.vannguyen.java_learn_ecom.modules.auth.domain.UserRole;

public record AuthenticatedUser(
        Long id,
        String email,
        UserRole role
) {
}