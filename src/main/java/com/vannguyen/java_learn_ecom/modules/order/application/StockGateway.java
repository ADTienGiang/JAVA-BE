package com.vannguyen.java_learn_ecom.modules.order.application;

public interface StockGateway {

    void decrease(Long productId, Long variantId, int quantity);

    void restore(Long productId, Long variantId, int quantity);
}