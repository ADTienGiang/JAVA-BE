package com.vannguyen.java_learn_ecom.modules.auth.application;

public record LoginCommand(
        String email,
        String password
) {
}