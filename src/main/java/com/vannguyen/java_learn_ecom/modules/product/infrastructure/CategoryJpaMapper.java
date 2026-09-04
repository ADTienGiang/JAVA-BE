package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.modules.product.domain.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryJpaMapper {

    public CategoryJpaEntity toEntity(Category category) {
        return new CategoryJpaEntity(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.isActive()
        );
    }

    public Category toDomain(CategoryJpaEntity entity) {
        Category category = new Category(
                entity.getId(),
                entity.getName(),
                entity.getSlug()
        );

        if (!entity.isActive()) {
            category.deactivate();
        }

        return category;
    }
}