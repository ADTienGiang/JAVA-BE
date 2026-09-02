package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.modules.product.application.CreateProductCommand;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductSortField;
import com.vannguyen.java_learn_ecom.modules.product.domain.Product;
import org.springframework.stereotype.Component;
import com.vannguyen.java_learn_ecom.modules.product.application.UpdateProductCommand;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductSearchQuery;
import com.vannguyen.java_learn_ecom.common.dto.PageQuery;
import com.vannguyen.java_learn_ecom.common.dto.SortDirection;
@Component
public class ProductMapper {

    public CreateProductCommand toCommand(CreateProductRequest request) {
        return new CreateProductCommand(
                request.categoryId(),
                request.name(),
                request.description(),
                request.price()
        );
    }

    public UpdateProductCommand toCommand(Long id, UpdateProductRequest request) {
        return new UpdateProductCommand(
                id,
                request.categoryId(),
                request.name(),
                request.description(),
                request.price()
        );
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCategoryId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStatus()
        );
    }

    public ProductSearchQuery toQuery(ProductSearchRequest request) {
        return new ProductSearchQuery(
                request.keyword(),
                request.categoryId(),
                request.minPrice(),
                request.maxPrice(),
                new PageQuery(
                        request.page() == null ? 1 : request.page(),
                        request.size() == null ? 20 : request.size()
                ),
                request.sortField() == null ? ProductSortField.NAME : request.sortField(),
                request.sortDirection() == null ? SortDirection.ASC : request.sortDirection()
        );
    }
}