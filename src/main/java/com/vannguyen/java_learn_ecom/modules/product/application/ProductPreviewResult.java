package com.vannguyen.java_learn_ecom.modules.product.application;

import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;

import java.math.BigDecimal;

public record ProductPreviewResult (
        String name,
        BigDecimal price,
        ProductStatus status
){
}
