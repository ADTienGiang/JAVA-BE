package com.vannguyen.java_learn_ecom.modules.product.domain;

import java.util.List;
import java.util.Optional;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductSearchQuery;
import com.vannguyen.java_learn_ecom.common.dto.PageResult;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAllActive();

    PageResult<Product> search(ProductSearchQuery query);
}