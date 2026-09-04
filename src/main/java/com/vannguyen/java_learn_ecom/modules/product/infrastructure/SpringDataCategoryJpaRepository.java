package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataCategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {

    boolean existsBySlug(String slug);

    List<CategoryJpaEntity> findAllByActiveTrue();
}