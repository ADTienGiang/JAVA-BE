package com.vannguyen.java_learn_ecom.modules.auth.application;

public record RegisterCommand(
        String email,
        String password
) {
}