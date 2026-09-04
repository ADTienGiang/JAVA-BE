package com.vannguyen.java_learn_ecom.modules.product.domain;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository {

    ProductVariant save(ProductVariant variant);

    Optional<ProductVariant> findById(Long id);

    List<ProductVariant> findAllActiveByProductId(Long productId);

    boolean existsBySku(String sku);
}