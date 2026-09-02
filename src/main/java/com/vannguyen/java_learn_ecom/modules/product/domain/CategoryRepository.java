package com.vannguyen.java_learn_ecom.modules.product.domain;

import java.util.Optional;
import java.util.List;
public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(Long id);

    boolean existsBySlug(String slug);

    List<Category> findAllActive();
}