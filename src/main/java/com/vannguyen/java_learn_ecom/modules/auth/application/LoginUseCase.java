package com.vannguyen.java_learn_ecom.modules.auth.application;

import com.vannguyen.java_learn_ecom.common.exception.BusinessException;
import com.vannguyen.java_learn_ecom.modules.auth.domain.UserAccount;
import com.vannguyen.java_learn_ecom.modules.auth.domain.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class LoginUseCase {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginUseCase(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UserAccount login(LoginCommand command) {
        String email = command.email().trim().toLowerCase(Locale.ROOT);

        UserAccount userAccount = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Invalid email or password"));

        if (!userAccount.isActive()) {
            throw new BusinessException("User account is inactive");
        }

        if (!passwordEncoder.matches(command.password(), userAccount.getPasswordHash())) {
            throw new BusinessException("Invalid email or password");
        }

        return userAccount;
    }
}