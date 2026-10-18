package com.vannguyen.java_learn_ecom.modules.auth.presentation;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank
        String refreshToken
) {
}