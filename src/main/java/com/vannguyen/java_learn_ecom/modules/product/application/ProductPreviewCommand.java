package com.vannguyen.java_learn_ecom.modules.product.application;

import java.math.BigDecimal;

public record ProductPreviewCommand(
        String name,
        BigDecimal price
) {
}