package com.vannguyen.java_learn_ecom.modules.auth.infrastructure;

import com.vannguyen.java_learn_ecom.modules.auth.domain.UserAccount;
import com.vannguyen.java_learn_ecom.modules.auth.domain.UserAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserAccountRepository implements UserAccountRepository {

    private final SpringDataUserAccountJpaRepository springDataUserAccountJpaRepository;
    private final UserAccountJpaMapper userAccountJpaMapper;

    public JpaUserAccountRepository(
            SpringDataUserAccountJpaRepository springDataUserAccountJpaRepository,
            UserAccountJpaMapper userAccountJpaMapper
    ) {
        this.springDataUserAccountJpaRepository = springDataUserAccountJpaRepository;
        this.userAccountJpaMapper = userAccountJpaMapper;
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        UserAccountJpaEntity entity = userAccountJpaMapper.toEntity(userAccount);
        UserAccountJpaEntity savedEntity = springDataUserAccountJpaRepository.save(entity);

        return userAccountJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserAccount> findByEmail(String email) {
        return springDataUserAccountJpaRepository.findByEmail(email)
                .map(userAccountJpaMapper::toDomain);
    }

    @Override
    public Optional<UserAccount> findById(Long id) {
        return springDataUserAccountJpaRepository.findById(id)
                .map(userAccountJpaMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataUserAccountJpaRepository.existsByEmail(email);
    }
}