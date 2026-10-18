package com.vannguyen.java_learn_ecom.modules.auth.presentation;

import com.vannguyen.java_learn_ecom.modules.auth.domain.UserRole;

import java.util.List;

public record AuthUserResponse(
        Long id,
        String email,
        UserRole role,
        List<String> authorities
) {
}