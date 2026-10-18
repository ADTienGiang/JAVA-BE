package com.vannguyen.java_learn_ecom.modules.auth.presentation;

public record RefreshTokenResponse(
        String accessToken,
        String tokenType
) {
}