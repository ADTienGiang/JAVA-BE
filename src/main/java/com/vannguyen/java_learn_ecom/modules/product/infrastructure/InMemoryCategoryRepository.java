package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.modules.product.domain.Category;
import com.vannguyen.java_learn_ecom.modules.product.domain.CategoryRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
public class InMemoryCategoryRepository implements CategoryRepository {

    private final Map<Long, Category> categories = new HashMap<>();
    private long nextId = 1L;

    @Override
    public Category save(Category category) {
        if (category.getId() == null) {
            Category categoryToSave = new Category(
                    nextId,
                    category.getName(),
                    category.getSlug()
            );

            categories.put(nextId, categoryToSave);
            nextId++;

            return categoryToSave;
        }

        categories.put(category.getId(), category);
        return category;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(categories.get(id));
    }

    @Override
    public boolean existsBySlug(String slug) {
        return categories.values()
                .stream()
                .anyMatch(category -> category.getSlug().equals(slug));
    }
    
    @Override
    public List<Category> findAllActive() {
        return categories.values()
                .stream()
                .filter(Category::isActive)
                .toList();
    }
}