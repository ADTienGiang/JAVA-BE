package com.vannguyen.java_learn_ecom.modules.auth.presentation;

import com.vannguyen.java_learn_ecom.modules.auth.domain.UserRole;

public record AuthUserResponse(
        Long id,
        String email,
        UserRole role
) {
}