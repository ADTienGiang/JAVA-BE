package com.vannguyen.java_learn_ecom.modules.product.infrastructure;

import com.vannguyen.java_learn_ecom.modules.product.domain.Product;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductRepository;
import com.vannguyen.java_learn_ecom.modules.product.domain.ProductStatus;
import com.vannguyen.java_learn_ecom.modules.product.application.ProductSearchQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.vannguyen.java_learn_ecom.common.dto.PageResult;

import java.util.Comparator;
public class InMemoryProductRepository implements ProductRepository {

    private final Map<Long, Product> products = new HashMap<>();
    private long nextId = 1L;

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            Product productToSave = new Product(
                    nextId,
                    product.getCategoryId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStatus()
            );

            products.put(nextId, productToSave);
            nextId++;

            return productToSave;
        }

        products.put(product.getId(), product);
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAllActive() {
        return products.values()
                .stream()
                .filter(product -> product.getStatus() == ProductStatus.ACTIVE)
                .toList();
    }

    @Override
    public PageResult<Product> search(ProductSearchQuery query) {
        Comparator<Product> comparator = buildComparator(query);

        List<Product> filteredProducts = products.values()
                .stream()
                .filter(product -> product.getStatus() == ProductStatus.ACTIVE)
                .filter(product -> matchesKeyword(product, query.keyword()))
                .filter(product -> matchesCategory(product, query.categoryId()))
                .filter(product -> matchesMinPrice(product, query.minPrice()))
                .filter(product -> matchesMaxPrice(product, query.maxPrice()))
                .sorted(comparator)
                .toList();

        List<Product> pageItems = filteredProducts.stream()
                .skip(query.pageQuery().offset())
                .limit(query.pageQuery().size())
                .toList();

        return PageResult.of(
                pageItems,
                query.pageQuery(),
                filteredProducts.size()
        );
    }

    private boolean matchesKeyword(Product product, String keyword) {
        if (isBlank(keyword)) {
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

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
    private Comparator<Product> buildComparator(ProductSearchQuery query) {
        Comparator<Product> comparator = switch (query.sortField()) {
            case NAME -> Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
            case PRICE -> Comparator.comparing(Product::getPrice);
        };

        if (!query.sortDirection().isAscending()) {
            return comparator.reversed();
        }

        return comparator;
    }
    

}