package com.vannguyen.java_learn_ecom.modules.product.application;

import com.vannguyen.java_learn_ecom.common.dto.PageQuery;
import com.vannguyen.java_learn_ecom.common.dto.SortDirection;

import java.math.BigDecimal;

public record ProductSearchQuery(
        String keyword,
        Long categoryId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        PageQuery pageQuery,
        ProductSortField sortField,
        SortDirection sortDirection
) {
}