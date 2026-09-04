package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.modules.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductJpaMapper {

    public ProductJpaEntity toEntity(Product product) {
        return new ProductJpaEntity(
                product.getId(),
                product.getCategoryId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStatus()
        );
    }

    public Product toDomain(ProductJpaEntity entity) {
        return new Product(
                entity.getId(),
                entity.getCategoryId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStatus()
        );
    }
}