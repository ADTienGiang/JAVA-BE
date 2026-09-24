package com.vannguyen.java_learn_ecom.modules.auth.infrastructure;

import com.vannguyen.java_learn_ecom.modules.auth.domain.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class UserAccountJpaMapper {

    public UserAccountJpaEntity toEntity(UserAccount userAccount) {
        return new UserAccountJpaEntity(
                userAccount.getId(),
                userAccount.getEmail(),
                userAccount.getPasswordHash(),
                userAccount.getRole(),
                userAccount.isActive(),
                userAccount.getCreatedAt()
        );
    }

    public UserAccount toDomain(UserAccountJpaEntity entity) {
        return new UserAccount(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRole(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }
}