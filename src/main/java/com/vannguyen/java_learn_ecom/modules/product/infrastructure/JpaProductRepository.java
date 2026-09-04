package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.common.dto.PageResult;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductSearchQuery;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductSortField;
import com.vannguyen.java_learn_ecom.modules.product.domain.Product;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductRepository;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class JpaProductRepository implements ProductRepository {

    private final SpringDataProductJpaRepository springDataProductJpaRepository;
    private final ProductJpaMapper productJpaMapper;

    public JpaProductRepository(
            SpringDataProductJpaRepository springDataProductJpaRepository,
            ProductJpaMapper productJpaMapper
    ) {
        this.springDataProductJpaRepository = springDataProductJpaRepository;
        this.productJpaMapper = productJpaMapper;
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = productJpaMapper.toEntity(product);
        ProductJpaEntity savedEntity = springDataProductJpaRepository.save(entity);

        return productJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return springDataProductJpaRepository.findById(id)
                .map(productJpaMapper::toDomain);
    }

    @Override
    public List<Product> findAllActive() {
        return springDataProductJpaRepository.findAllByStatus(ProductStatus.ACTIVE)
                .stream()
                .map(productJpaMapper::toDomain)
                .toList();
    }

    @Override
    public PageResult<Product> search(ProductSearchQuery query) {
        List<Product> filteredProducts = springDataProductJpaRepository.findAll()
                .stream()
                .map(productJpaMapper::toDomain)
                .filter(product -> matchesKeyword(product, query.keyword()))
                .filter(product -> matchesCategory(product, query.categoryId()))
                .filter(product -> matchesMinPrice(product, query.minPrice()))
                .filter(product -> matchesMaxPrice(product, query.maxPrice()))
                .sorted(comparator(query))
                .toList();

        int fromIndex = Math.min(query.pageQuery().offset(), filteredProducts.size());
        int toIndex = Math.min(fromIndex + query.pageQuery().size(), filteredProducts.size());

        List<Product> pageItems = filteredProducts.subList(fromIndex, toIndex);

        return PageResult.of(
                pageItems,
                query.pageQuery(),
                filteredProducts.size()
        );
    }

    private boolean matchesKeyword(Product product, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }

        String normalizedKeyword = keyword.trim().toLowerCase();

        return product.getName().toLowerCase().contains(normalizedKeyword)
                || product.getDescription().toLowerCase().contains(normalizedKeyword);
    }

    private boolean matchesCategory(Product product, Long categoryId) {
        if (categoryId == null) {
            return true;
        }

        return product.getCategoryId().equals(categoryId);
    }

    private boolean matchesMinPrice(Product product, java.math.BigDecimal minPrice) {
        if (minPrice == null) {
            return true;
        }

        return product.getPrice().compareTo(minPrice) >= 0;
    }

    private boolean matchesMaxPrice(Product product, java.math.BigDecimal maxPrice) {
        if (maxPrice == null) {
            return true;
        }

        return product.getPrice().compareTo(maxPrice) <= 0;
    }

    private Comparator<Product> comparator(ProductSearchQuery query) {
        Comparator<Product> comparator = switch (query.sortField()) {
            case NAME -> Comparator.comparing(Product::getName);
            case PRICE -> Comparator.comparing(Product::getPrice);
        };

        if (query.sortDirection().isAscending()) {
            return comparator;
        }

        return comparator.reversed();
    }
}