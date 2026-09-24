package com.vannguyen.java_learn_ecom.modules.auth.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataUserAccountJpaRepository
        extends JpaRepository<UserAccountJpaEntity, Long> {

    Optional<UserAccountJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}