package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariant;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariantRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryProductVariantRepository implements ProductVariantRepository {

    private final Map<Long, ProductVariant> variants = new HashMap<>();
    private long nextId = 1L;

    @Override
    public ProductVariant save(ProductVariant variant) {
        if (variant.getId() == null) {
            ProductVariant variantToSave = new ProductVariant(
                    nextId,
                    variant.getProductId(),
                    variant.getSku(),
                    variant.getName(),
                    variant.getPrice(),
                    variant.isActive()
            );

            variants.put(nextId, variantToSave);
            nextId++;

            return variantToSave;
        }

        variants.put(variant.getId(), variant);
        return variant;
    }

    @Override
    public Optional<ProductVariant> findById(Long id) {
        return Optional.ofNullable(variants.get(id));
    }

    @Override
    public List<ProductVariant> findAllActiveByProductId(Long productId) {
        return variants.values()
                .stream()
                .filter(variant -> variant.getProductId().equals(productId))
                .filter(ProductVariant::isActive)
                .toList();
    }

    @Override
    public boolean existsBySku(String sku) {
        return variants.values()
                .stream()
                .anyMatch(variant -> variant.getSku().equals(sku.trim().toUpperCase()));
    }
}