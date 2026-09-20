package com.vannguyen.java_learn_ecom.modules.product.application;

import com.vannguyen.java_learn_ecom.modules.product.domain.Product;

import java.util.Optional;

public interface ProductCache {

    Optional<Product> getById(Long id);

    void put(Product product);

    void evictById(Long id);
}