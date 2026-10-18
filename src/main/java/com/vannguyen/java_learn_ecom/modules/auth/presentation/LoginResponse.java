package com.vannguyen.java_learn_ecom.modules.auth.presentation;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        AuthUserResponse user
) {
}