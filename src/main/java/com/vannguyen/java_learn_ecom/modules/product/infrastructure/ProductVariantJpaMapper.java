package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariant;
import org.springframework.stereotype.Component;

@Component
public class ProductVariantJpaMapper {

    public ProductVariantJpaEntity toEntity(ProductVariant productVariant) {
        return new ProductVariantJpaEntity(
                productVariant.getId(),
                productVariant.getProductId(),
                productVariant.getSku(),
                productVariant.getName(),
                productVariant.getPrice(),
                productVariant.isActive()
        );
    }

    public ProductVariant toDomain(ProductVariantJpaEntity entity) {
        ProductVariant productVariant = new ProductVariant(
                entity.getId(),
                entity.getProductId(),
                entity.getSku(),
                entity.getName(),
                entity.getPrice(),
                entity.isActive()
        );

        if (!entity.isActive()) {
            productVariant.deactivate();
        }

        return productVariant;
    }
}