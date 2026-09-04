package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataProductVariantJpaRepository extends JpaRepository<ProductVariantJpaEntity, Long> {

    boolean existsBySku(String sku);

    List<ProductVariantJpaEntity> findAllByProductIdAndActiveTrue(Long productId);
}