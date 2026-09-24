package com.vannguyen.java_learn_ecom.modules.auth.presentation;

import com.vannguyen.java_learn_ecom.common.api.ApiResponse;
import com.vannguyen.java_learn_ecom.modules.auth.application.RegisterCommand;
import com.vannguyen.java_learn_ecom.modules.auth.application.RegisterUseCase;
import com.vannguyen.java_learn_ecom.modules.auth.domain.UserAccount;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vannguyen.java_learn_ecom.modules.auth.application.LoginCommand;
import com.vannguyen.java_learn_ecom.modules.auth.application.LoginUseCase;
import com.vannguyen.java_learn_ecom.modules.auth.infrastructure.JwtTokenService;
import com.vannguyen.java_learn_ecom.modules.auth.application.AuthenticatedUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final JwtTokenService jwtTokenService;

    public AuthController(
            RegisterUseCase registerUseCase,
            LoginUseCase loginUseCase,
            JwtTokenService jwtTokenService
    ) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthUserResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        UserAccount userAccount = registerUseCase.register(
                new RegisterCommand(request.email(), request.password())
        );

        AuthUserResponse response = new AuthUserResponse(
                userAccount.getId(),
                userAccount.getEmail(),
                userAccount.getRole()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        UserAccount userAccount = loginUseCase.login(
                new LoginCommand(request.email(), request.password())
        );

        AuthUserResponse userResponse = new AuthUserResponse(
                userAccount.getId(),
                userAccount.getEmail(),
                userAccount.getRole()
        );

        LoginResponse response = new LoginResponse(
                jwtTokenService.generateAccessToken(userAccount),
                "Bearer",
                userResponse
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthUserResponse>> me(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        AuthUserResponse response = new AuthUserResponse(
                authenticatedUser.id(),
                authenticatedUser.email(),
                authenticatedUser.role()
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}