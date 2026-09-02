package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.modules.product.application.CreateCategoryCommand;
import com.vannguyen.java_learn_ecom.modules.product.domain.Category;
import org.springframework.stereotype.Component;
import com.vannguyen.java_learn_ecom.modules.product.application.UpdateCategoryCommand;
@Component
public class CategoryMapper {

    public CreateCategoryCommand toCommand(CreateCategoryRequest request) {
        return new CreateCategoryCommand(
                request.name(),
                request.slug()
        );
    }
    public UpdateCategoryCommand toCommand(Long id, UpdateCategoryRequest request) {
        return new UpdateCategoryCommand(
                id,
                request.name(),
                request.slug()
        );
    }
    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.isActive()
        );
    }




}