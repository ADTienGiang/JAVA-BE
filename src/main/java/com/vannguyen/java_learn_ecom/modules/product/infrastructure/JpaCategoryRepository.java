package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.modules.product.domain.Category;
import com.vannguyen.java_learn_ecom.modules.product.domain.CategoryRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class JpaCategoryRepository implements CategoryRepository {

    private final SpringDataCategoryJpaRepository springDataCategoryJpaRepository;
    private final CategoryJpaMapper categoryJpaMapper;

    public JpaCategoryRepository(
            SpringDataCategoryJpaRepository springDataCategoryJpaRepository,
            CategoryJpaMapper categoryJpaMapper
    ) {
        this.springDataCategoryJpaRepository = springDataCategoryJpaRepository;
        this.categoryJpaMapper = categoryJpaMapper;
    }

    @Override
    public Category save(Category category) {
        CategoryJpaEntity entity = categoryJpaMapper.toEntity(category);
        CategoryJpaEntity savedEntity = springDataCategoryJpaRepository.save(entity);

        return categoryJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return springDataCategoryJpaRepository.findById(id)
                .map(categoryJpaMapper::toDomain);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return springDataCategoryJpaRepository.existsBySlug(slug);
    }

    @Override
    public List<Category> findAllActive() {
        return springDataCategoryJpaRepository.findAllByActiveTrue()
                .stream()
                .map(categoryJpaMapper::toDomain)
                .toList();
    }
}