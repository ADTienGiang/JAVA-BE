package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductVariantStock;
import org.springframework.stereotype.Component;
import com.vannguyen.java_learn_ecom.modules.product.application.DecreaseProductVariantStockCommand;
@Component
public class ProductVariantStockJpaMapper {

    public ProductVariantStockJpaEntity toEntity(ProductVariantStock stock) {
        return new ProductVariantStockJpaEntity(
                stock.getId(),
                stock.getVariantId(),
                stock.getQuantity(),
                stock.isAvailable()
        );
    }

    public ProductVariantStock toDomain(ProductVariantStockJpaEntity entity) {
        return new ProductVariantStock(
                entity.getId(),
                entity.getVariantId(),
                entity.getQuantity(),
                entity.isAvailable()
        );
    }


}