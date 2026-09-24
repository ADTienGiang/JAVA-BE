package com.vannguyen.java_learn_ecom.modules.auth.domain;

import java.util.Optional;

public interface UserAccountRepository {

    UserAccount save(UserAccount userAccount);

    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findById(Long id);

    boolean existsByEmail(String email);
}