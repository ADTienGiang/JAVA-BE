package com.vannguyen.java_learn_ecom.modules.product.application;

import com.vannguyen.java_learn_ecom.common.exception.BusinessException;
import com.vannguyen.java_learn_ecom.modules.product.domain.Category;
import com.vannguyen.java_learn_ecom.modules.product.domain.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.vannguyen.java_learn_ecom.common.exception.ResourceNotFoundException;
import java.util.List;
@Service
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(CreateCategoryCommand command) {
        log.info("Create category request received: name={}, slug={}", command.name(), command.slug());

        if (categoryRepository.existsBySlug(command.slug())) {
            log.warn("Create category rejected because slug already exists: slug={}", command.slug());
            throw new BusinessException("Category slug already exists");
        }

        Category category = new Category(
                null,
                command.name(),
                command.slug()
        );

        Category savedCategory = categoryRepository.save(category);

        log.info("Category created successfully: id={}, slug={}", savedCategory.getId(), savedCategory.getSlug());

        return savedCategory;
    }

    public Category findById(Long id) {
        log.info("Find category by id: id={}", id);

        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category not found: id={}", id);
                    return new ResourceNotFoundException("Category not found");
                });
    }

    public List<Category> findAllActive() {
        log.info("Find all active categories");

        return categoryRepository.findAllActive();
    }

    public Category update(UpdateCategoryCommand command) {
        log.info("Update category request received: id={}, name={}, slug={}",
                command.id(), command.name(), command.slug());

        Category category = findById(command.id());

        if (!category.getSlug().equals(command.slug()) && categoryRepository.existsBySlug(command.slug())) {
            log.warn("Update category rejected because slug already exists: id={}, slug={}",
                    command.id(), command.slug());
            throw new BusinessException("Category slug already exists");
        }

        category.rename(command.name(), command.slug());

        Category savedCategory = categoryRepository.save(category);

        log.info("Category updated successfully: id={}, slug={}",
                savedCategory.getId(), savedCategory.getSlug());

        return savedCategory;
    }

    public Category deactivate(Long id) {
        log.info("Deactivate category request received: id={}", id);

        Category category = findById(id);

        category.deactivate();

        Category savedCategory = categoryRepository.save(category);

        log.info("Category deactivated successfully: id={}", savedCategory.getId());

        return savedCategory;
    }

}