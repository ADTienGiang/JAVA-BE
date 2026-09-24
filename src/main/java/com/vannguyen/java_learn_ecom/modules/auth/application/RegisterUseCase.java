package com.vannguyen.java_learn_ecom.modules.auth.application;

import com.vannguyen.java_learn_ecom.common.exception.BusinessException;
import com.vannguyen.java_learn_ecom.modules.auth.domain.UserAccount;
import com.vannguyen.java_learn_ecom.modules.auth.domain.UserAccountRepository;
import com.vannguyen.java_learn_ecom.modules.auth.domain.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;

@Service
public class RegisterUseCase {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUseCase(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserAccount register(RegisterCommand command) {
        String email = command.email().trim().toLowerCase(Locale.ROOT);

        if (userAccountRepository.existsByEmail(email)) {
            throw new BusinessException("Email already registered");
        }

        String passwordHash = passwordEncoder.encode(command.password());

        UserAccount userAccount = new UserAccount(
                null,
                email,
                passwordHash,
                UserRole.CUSTOMER,
                true,
                Instant.now()
        );

        return userAccountRepository.save(userAccount);
    }
}