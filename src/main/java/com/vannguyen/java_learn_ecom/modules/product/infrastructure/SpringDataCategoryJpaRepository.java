package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
public interface SpringDataCategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {

    boolean existsBySlug(String slug);

    List<CategoryJpaEntity> findAllByActiveTrue();

    @EntityGraph(attributePaths = "products")
    List<CategoryJpaEntity> findWithProductsByActiveTrue();

}