package com.vannguyen.java_learn_ecom.modules.product.domain;

import java.util.Optional;

public interface ProductVariantStockRepository {

    ProductVariantStock save(ProductVariantStock stock);

    Optional<ProductVariantStock> findByVariantId(Long variantId);

    boolean existsByVariantId(Long variantId);
    
    boolean decreaseIfEnough(Long variantId, int quantity);

    boolean restore(Long variantId, int quantity);

    boolean updateAvailability(Long variantId, boolean available);
}