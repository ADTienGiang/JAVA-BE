package com.vannguyen.java_learn_ecom.modules.order.application;

public interface ProductCatalogGateway {

    ProductItemSnapshot getSellableProductItem(Long productId, Long variantId);
}