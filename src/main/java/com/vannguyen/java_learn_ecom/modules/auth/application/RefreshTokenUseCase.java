package com.vannguyen.java_learn_ecom.modules.auth.application;

import com.vannguyen.java_learn_ecom.common.exception.BusinessException;
import com.vannguyen.java_learn_ecom.modules.auth.domain.UserAccount;
import com.vannguyen.java_learn_ecom.modules.auth.domain.UserAccountRepository;
import com.vannguyen.java_learn_ecom.modules.auth.infrastructure.JwtTokenService;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenUseCase {

    private final JwtTokenService jwtTokenService;
    private final UserAccountRepository userAccountRepository;

    public RefreshTokenUseCase(
            JwtTokenService jwtTokenService,
            UserAccountRepository userAccountRepository
    ) {
        this.jwtTokenService = jwtTokenService;
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional(readOnly = true)
    public String refreshAccessToken(String refreshToken) {
        try {
            Claims claims = jwtTokenService.parseClaims(refreshToken);

            if (!"refresh".equals(claims.get("type", String.class))) {
                throw new BusinessException("Invalid refresh token");
            }

            Long userId = Long.valueOf(claims.getSubject());

            UserAccount userAccount = userAccountRepository.findById(userId)
                    .filter(UserAccount::isActive)
                    .orElseThrow(() -> new BusinessException("Invalid refresh token"));

            return jwtTokenService.generateAccessToken(userAccount);
        } catch (BusinessException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BusinessException("Invalid refresh token");
        }
    }
}