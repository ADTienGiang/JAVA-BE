package com.vannguyen.java_learn_ecom.modules.product.presentation;

import com.vannguyen.java_learn_ecom.common.dto.SortDirection;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductSortField;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductSearchRequest(

        @Size(max = 100, message = "Keyword must not exceed 100 characters")
        String keyword,

        @Positive(message = "Category id must be greater than zero")
        Long categoryId,

        @Positive(message = "Minimum price must be greater than zero")
        BigDecimal minPrice,

        @Positive(message = "Maximum price must be greater than zero")
        BigDecimal maxPrice,

        @Positive(message = "Page must be greater than zero")
        Integer page,

        @Positive(message = "Size must be greater than zero")
        Integer size,

        ProductSortField sortField,

        SortDirection sortDirection
) {
}